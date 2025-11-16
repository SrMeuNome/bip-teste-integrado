import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Beneficio } from '../../../domain/models/beneficio.model';
import { environment } from '../../../environments/enviroment';
import { BeneficioRepository } from '../../../domain/repositories/beneficio.repository';
import { ApiResponse } from '../../../domain/models/api-response.model';
import { CreateBeneficioDTO, TransferBeneficioDTO, UpdateBeneficioDTO } from '../../../domain/dtos';
import { ENDPOINTS } from '../../../domain/constants/endpoints';

@Injectable({
  providedIn: 'root'
})
export class BeneficioHttpRepository extends BeneficioRepository {
  private readonly baseUrl = `${environment.apiUrl}${ENDPOINTS.BASE_API_V1_URL}${ENDPOINTS.BENEFICIOS.BASE}`;

  constructor(private http: HttpClient) {
    super();
  }

  findAll(): Observable<Beneficio[]> {
    return this.http
      .get<ApiResponse<Beneficio[]>>(this.baseUrl)
      .pipe(map(response => response.data));
  }

  findById(id: number): Observable<Beneficio> {
    return this.http
      .get<ApiResponse<Beneficio>>(`${this.baseUrl}/${id}`)
      .pipe(map(response => response.data));
  }

  create(dto: CreateBeneficioDTO): Observable<Beneficio> {
    return this.http
      .post<ApiResponse<Beneficio>>(this.baseUrl, dto)
      .pipe(map(response => response.data));
  }

  update(id: number, dto: UpdateBeneficioDTO): Observable<Beneficio> {
    return this.http
      .put<ApiResponse<Beneficio>>(`${this.baseUrl}/${id}`, dto)
      .pipe(map(response => response.data));
  }

  delete(id: number): Observable<void> {
    return this.http
      .delete<ApiResponse<void>>(`${this.baseUrl}/${id}`)
      .pipe(map(() => void 0));
  }

  transfer(dto: TransferBeneficioDTO): Observable<void> {
    return this.http
      .post<ApiResponse<void>>(`${this.baseUrl}${ENDPOINTS.BENEFICIOS.TRANSFERIR}`, dto)
      .pipe(map(() => void 0));
  }
}