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
  recentAppointments: any[];
}

export interface ReceptionistDashboard {
  appointmentsToday: any[];
  pendingConfirmations: any[];
  stylistsAvailability: Array<{
    id: number;
    name: string;
    available: boolean;
  }>;
}

export interface StylistDashboard {
  nextAppointment: any;
  appointmentsToday: any[];
  appointmentsUpcoming: any[];
  totalCompletedThisMonth: number;
}

export interface ClientDashboard {
  upcomingAppointments: any[];
  pastAppointments: any[];
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
