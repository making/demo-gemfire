import { Customer, CustomerCreateRequest, CustomerUpdateRequest } from '../types/customer';

const API_BASE = '/api/customers';

export const customerApi = {
  getCustomers: async (): Promise<Customer[]> => {
    const response = await fetch(API_BASE);
    if (!response.ok) {
      throw new Error('Failed to fetch customers');
    }
    return response.json();
  },

  getCustomer: async (id: number): Promise<Customer> => {
    const response = await fetch(`${API_BASE}/${id}`);
    if (!response.ok) {
      throw new Error('Failed to fetch customer');
    }
    return response.json();
  },

  createCustomer: async (customer: CustomerCreateRequest): Promise<Customer> => {
    const response = await fetch(API_BASE, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(customer),
    });
    if (!response.ok) {
      throw new Error('Failed to create customer');
    }
    return response.json();
  },

  updateCustomer: async (id: number, updates: CustomerUpdateRequest): Promise<Customer> => {
    const response = await fetch(`${API_BASE}/${id}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(updates),
    });
    if (!response.ok) {
      throw new Error('Failed to update customer');
    }
    return response.json();
  },

  deleteCustomer: async (id: number): Promise<void> => {
    const response = await fetch(`${API_BASE}/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) {
      throw new Error('Failed to delete customer');
    }
  },
};