import { useState, useEffect } from 'react';
import { customerApi } from '../api/customerApi';
import { Customer, CustomerCreateRequest, CustomerUpdateRequest } from '../types/customer';
import { Button } from './ui/Button';
import { Input } from './ui/Input';

interface CustomerFormProps {
  customer?: Customer;
  onSuccess: () => void;
  onCancel: () => void;
}

export const CustomerForm = ({ customer, onSuccess, onCancel }: CustomerFormProps) => {
  const [formData, setFormData] = useState({
    id: customer?.id || 0,
    name: customer?.name || '',
  });
  const [errors, setErrors] = useState<{ id?: string; name?: string }>({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const isEditing = !!customer;

  useEffect(() => {
    if (customer) {
      setFormData({
        id: customer.id,
        name: customer.name,
      });
    }
  }, [customer]);

  const validateForm = () => {
    const newErrors: { id?: string; name?: string } = {};
    
    if (!isEditing && (!formData.id || formData.id <= 0)) {
      newErrors.id = 'ID must be a positive number';
    }
    
    if (!formData.name.trim()) {
      newErrors.name = 'Name is required';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!validateForm()) return;
    
    setIsSubmitting(true);
    
    try {
      if (isEditing) {
        const updateRequest: CustomerUpdateRequest = {
          name: formData.name.trim(),
        };
        await customerApi.updateCustomer(customer.id, updateRequest);
      } else {
        const createRequest: CustomerCreateRequest = {
          id: formData.id,
          name: formData.name.trim(),
        };
        await customerApi.createCustomer(createRequest);
      }
      
      onSuccess();
    } catch (error) {
      console.error('Failed to save customer:', error);
      alert('Failed to save customer. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {!isEditing && (
        <Input
          label="ID"
          type="number"
          value={formData.id}
          onChange={(e) => setFormData({ ...formData, id: parseInt(e.target.value) || 0 })}
          error={errors.id}
          min="1"
          required
        />
      )}
      
      <Input
        label="Name"
        type="text"
        value={formData.name}
        onChange={(e) => setFormData({ ...formData, name: e.target.value })}
        error={errors.name}
        required
      />
      
      <div className="flex justify-end space-x-3 pt-4">
        <Button
          type="button"
          variant="secondary"
          onClick={onCancel}
          disabled={isSubmitting}
        >
          Cancel
        </Button>
        <Button
          type="submit"
          disabled={isSubmitting}
        >
          {isSubmitting 
            ? (isEditing ? 'Updating...' : 'Creating...') 
            : (isEditing ? 'Update' : 'Create')
          }
        </Button>
      </div>
    </form>
  );
};