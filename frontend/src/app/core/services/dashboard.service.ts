import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { 
  AdminDashboard, 
  ReceptionistDashboard, 
  StylistDashboard, 
  ClientDashboard 
} from '../../shared/models/dashboard.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private apiUrl = `${environment.apiUrl}/dashboard`;

  constructor(private http: HttpClient) {}

  getAdminDashboard(): Observable<AdminDashboard> {
    return this.http.get<AdminDashboard>(this.apiUrl);
  }

  getReceptionistDashboard(): Observable<ReceptionistDashboard> {
    return this.http.get<ReceptionistDashboard>(this.apiUrl);
  }

  getStylistDashboard(): Observable<StylistDashboard> {
    return this.http.get<StylistDashboard>(this.apiUrl);
  }

  getClientDashboard(): Observable<ClientDashboard> {
    return this.http.get<ClientDashboard>(this.apiUrl);
  }
}
