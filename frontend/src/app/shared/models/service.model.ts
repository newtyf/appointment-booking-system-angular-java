export interface Service {
  id: number;
  name: string;
  durationMin: number;
  description: string;
  price: number;
}

export interface ServiceCreateRequest {
  name: string;
  durationMin: number;
  description: string;
  price: number;
}

export interface ServiceUpdateRequest {
  name?: string;
  durationMin?: number;
  description?: string;
  price?: number;
}
