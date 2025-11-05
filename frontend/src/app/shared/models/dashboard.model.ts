import { Appointment } from './appointment.model';

export interface AdminDashboard {
  appointmentsStats: {
    total: number;
    pending: number;
    confirmed: number;
    completed: number;
    cancelled: number;
  };
  totalClients: number;
  totalStylists: number;
  topStylists: Array<{
    id: number;
    name: string;
    appointmentsCount: number;
  }>;
  topServices: Array<{
    id: number;
    name: string;
    bookingsCount: number;
  }>;
  recentAppointments: Appointment[];
}

export interface ReceptionistDashboard {
  appointmentsToday: Appointment[];
  pendingConfirmations: Appointment[];
  stylistsAvailability: Array<{
    id: number;
    name: string;
    available: boolean;
  }>;
}

export interface StylistDashboard {
  nextAppointment: Appointment | null;
  appointmentsToday: Appointment[];
  appointmentsUpcoming: Appointment[];
  totalCompletedThisMonth: number;
}

export interface ClientDashboard {
  upcomingAppointments: Appointment[];
  pastAppointments: Appointment[];
  totalAppointments: number;
  favoriteService: {
    id: number;
    name: string;
  } | null;
}

export interface AvailabilitySlot {
  time: string;
  available: boolean;
  stylistId?: number;
  stylistName?: string;
}

export interface AvailabilityRequest {
  date: string;
  serviceId: number;
  stylistId?: number;
}

export interface AvailabilityResponse {
  date: string;
  slots: AvailabilitySlot[];
}
