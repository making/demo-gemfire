export interface Customer {
  id: number;
  name: string;
}

export interface CustomerCreateRequest {
  id: number;
  name: string;
}

export interface CustomerUpdateRequest {
  name: string;
}