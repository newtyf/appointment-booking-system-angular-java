import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface AiImageRequest {
  image: File;
  prompt: string;
}

export interface AiImageResponse {
  imageUrl: string;
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class AiService {
  private apiUrl = `${environment.apiUrl}/ai`;

  constructor(private http: HttpClient) {}

  processImage(image: File, prompt: string): Observable<AiImageResponse> {
    const formData = new FormData();
    formData.append('image', image);
    formData.append('prompt', prompt);

    return this.http.post<AiImageResponse>(`${this.apiUrl}/process-image`, formData);
  }
}
