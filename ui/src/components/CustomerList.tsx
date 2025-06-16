import { useState } from 'react';
import { useCustomers } from '../hooks/useCustomers';
import { customerApi } from '../api/customerApi';
import { Button } from './ui/Button';
import { CustomerForm } from './CustomerForm';
import { Modal } from './ui/Modal';
import { Customer } from '../types/customer';

export const CustomerList = () => {
  const { customers, isLoading, error, mutate } = useCustomers();
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [editingCustomer, setEditingCustomer] = useState<Customer | null>(null);
  const [deletingCustomerId, setDeletingCustomerId] = useState<number | null>(null);

  const handleDelete = async (id: number) => {
    if (!confirm('Are you sure you want to delete this customer?')) return;
    
    setDeletingCustomerId(id);
    try {
      await customerApi.deleteCustomer(id);
      mutate();
    } catch (error) {
      console.error('Failed to delete customer:', error);
      alert('Failed to delete customer');
    } finally {
      setDeletingCustomerId(null);
    }
  };

  const handleCreateSuccess = () => {
    setIsCreateModalOpen(false);
    mutate();
  };

  const handleEditSuccess = () => {
    setEditingCustomer(null);
    mutate();
  };

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-lg text-gray-600">Loading customers...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-lg text-red-600">Failed to load customers</div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-900">Customers</h1>
        <Button onClick={() => setIsCreateModalOpen(true)}>
          Add Customer
        </Button>
      </div>

      {!customers || customers.length === 0 ? (
        <div className="text-center py-12">
          <div className="text-gray-500 text-lg">No customers found</div>
          <Button 
            className="mt-4" 
            onClick={() => setIsCreateModalOpen(true)}
          >
            Create Your First Customer
          </Button>
        </div>
      ) : (
        <div className="bg-white shadow-sm border border-gray-200 rounded-lg overflow-hidden">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  ID
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Name
                </th>
                <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {customers.map((customer) => (
                <tr key={customer.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {customer.id}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {customer.name}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-2">
                    <Button
                      variant="secondary"
                      size="sm"
                      onClick={() => setEditingCustomer(customer)}
                    >
                      Edit
                    </Button>
                    <Button
                      variant="danger"
                      size="sm"
                      onClick={() => handleDelete(customer.id)}
                      disabled={deletingCustomerId === customer.id}
                    >
                      {deletingCustomerId === customer.id ? 'Deleting...' : 'Delete'}
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <Modal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
        title="Create Customer"
      >
        <CustomerForm
          onSuccess={handleCreateSuccess}
          onCancel={() => setIsCreateModalOpen(false)}
        />
      </Modal>

      <Modal
        isOpen={!!editingCustomer}
        onClose={() => setEditingCustomer(null)}
        title="Edit Customer"
      >
        {editingCustomer && (
          <CustomerForm
            customer={editingCustomer}
            onSuccess={handleEditSuccess}
            onCancel={() => setEditingCustomer(null)}
          />
        )}
      </Modal>
    </div>
  );
};