import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Appointment, AppointmentCreateRequest, AppointmentUpdateRequest } from '../../shared/models/appointment.model';
import { AvailabilityRequest, AvailabilityResponse } from '../../shared/models/dashboard.model';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {
  private apiUrl = `${environment.apiUrl}/appointments`;

  constructor(private http: HttpClient) {}

  // List all appointments (Admin/Receptionist)
  getAllAppointments(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(this.apiUrl);
  }

  // Get role-based appointments
  getMyAppointments(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.apiUrl}/my-appointments`);
  }

  // Get appointment by ID
  getAppointmentById(id: number): Observable<Appointment> {
    return this.http.get<Appointment>(`${this.apiUrl}/${id}`);
  }

  // Get appointments by client
  getAppointmentsByClient(clientId: number): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.apiUrl}/client/${clientId}`);
  }

  // Get appointments by stylist
  getAppointmentsByStylist(stylistId: number): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.apiUrl}/stylist/${stylistId}`);
  }

  // Check availability for a date and service
  getAvailability(request: AvailabilityRequest): Observable<AvailabilityResponse> {
    let params = new HttpParams()
      .set('date', request.date)
      .set('serviceId', request.serviceId.toString());
    
    if (request.stylistId) {
      params = params.set('stylistId', request.stylistId.toString());
    }

    return this.http.get<AvailabilityResponse>(`${this.apiUrl}/availability`, { params });
  }

  // Create appointment (Admin/Receptionist)
  createAppointment(request: AppointmentCreateRequest): Observable<Appointment> {
    return this.http.post<Appointment>(this.apiUrl, request);
  }

  // Book appointment (Client self-booking)
  bookAppointment(request: AppointmentCreateRequest): Observable<Appointment> {
    return this.http.post<Appointment>(`${this.apiUrl}/book`, request);
  }

  // Create walk-in appointment
  createWalkInAppointment(request: AppointmentCreateRequest): Observable<Appointment> {
    return this.http.post<Appointment>(`${this.apiUrl}/walk-in`, request);
  }

  // Update appointment
  updateAppointment(id: number, request: AppointmentUpdateRequest): Observable<Appointment> {
    return this.http.put<Appointment>(`${this.apiUrl}/${id}`, request);
  }

  // Update appointment status
  updateAppointmentStatus(id: number, status: string): Observable<Appointment> {
    return this.http.patch<Appointment>(`${this.apiUrl}/${id}/status`, { status });
  }

  // Cancel/Delete appointment
  deleteAppointment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
