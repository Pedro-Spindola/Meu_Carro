import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { VeiculoResponse, VeiculoCreateRequest, TipoCombustivel } from '../../models/veiculo.model';

@Component({
  selector: 'app-veiculo-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './veiculo-form.html',
  styleUrl: './veiculo-form.scss'
})
export class VeiculoFormComponent implements OnInit {
  @Input() veiculo?: VeiculoResponse;
  @Input() isLoading = signal(false);
  @Output() save = new EventEmitter<VeiculoCreateRequest>();
  @Output() cancel = new EventEmitter<void>();
  @Output() delete = new EventEmitter<number>();

  form!: FormGroup;
  tiposCombustivel = Object.values(TipoCombustivel);

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      marca: [this.veiculo?.marca || '', [Validators.required]],
      modelo: [this.veiculo?.modelo || '', [Validators.required]],
      ano: [this.veiculo?.ano || new Date().getFullYear(), [Validators.required, Validators.min(1900)]],
      placa: [this.veiculo?.placa || '', [Validators.required, Validators.pattern(/^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$/)]],
      combustivel: [this.veiculo?.combustivel || TipoCombustivel.GASOLINA, [Validators.required]],
      quilometragemAtual: [this.veiculo?.quilometragemAtual || 0, [Validators.required, Validators.min(0)]]
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.save.emit(this.form.value);
    }
  }

  onDelete(): void {
    if (this.veiculo && confirm(`Tem certeza que deseja excluir o veículo ${this.veiculo.marca} ${this.veiculo.modelo}?`)) {
      this.delete.emit(this.veiculo.id);
    }
  }

  onCancel(): void {
    this.cancel.emit();
  }
}
