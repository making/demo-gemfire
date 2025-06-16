import useSWR from 'swr';
import { customerApi } from '../api/customerApi';

export const useCustomers = () => {
  const { data, error, mutate } = useSWR('/api/customers', customerApi.getCustomers);

  return {
    customers: data,
    isLoading: !error && !data,
    error,
    mutate,
  };
};

export const useCustomer = (id: number) => {
  const { data, error, mutate } = useSWR(
    id ? `/api/customers/${id}` : null,
    () => customerApi.getCustomer(id)
  );

  return {
    customer: data,
    isLoading: !error && !data,
    error,
    mutate,
  };
};