import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Appointment, AppointmentCreateRequest, AppointmentUpdateRequest } from '../../shared/models/appointment.model';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {
  private apiUrl = `${environment.apiUrl}/appointments`;

  constructor(private http: HttpClient) {}

  getAllAppointments(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(this.apiUrl);
  }

  getAppointmentById(id: number): Observable<Appointment> {
    return this.http.get<Appointment>(`${this.apiUrl}/${id}`);
  }

  getAppointmentsByClient(clientId: number): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.apiUrl}/client/${clientId}`);
  }

  getAppointmentsByStylist(stylistId: number): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.apiUrl}/stylist/${stylistId}`);
  }

  createAppointment(request: AppointmentCreateRequest): Observable<Appointment> {
    return this.http.post<Appointment>(this.apiUrl, request);
  }

  updateAppointment(id: number, request: AppointmentUpdateRequest): Observable<Appointment> {
    return this.http.put<Appointment>(`${this.apiUrl}/${id}`, request);
  }

  deleteAppointment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
