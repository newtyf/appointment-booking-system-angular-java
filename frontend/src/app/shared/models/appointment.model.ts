export interface Appointment {
  id: number;
  clientId?: number;
  clientName?: string;
  clientPhone?: string;
  clientEmail?: string;
  isWalkIn: boolean;
  stylistId: number;
  serviceId: number;
  date: string;
  status: string;
  createdBy: number;
  modifiedBy: number;
  createdAt: string;
  updatedAt: string;
}

export interface AppointmentCreateRequest {
  clientId?: number;
  clientName?: string;
  clientPhone?: string;
  clientEmail?: string;
  isWalkIn: boolean;
  stylistId: number;
  serviceId: number;
  date: string;
  status?: string;
}

export interface AppointmentUpdateRequest {
  stylistId?: number;
  serviceId?: number;
  date?: string;
  status?: string;
}
