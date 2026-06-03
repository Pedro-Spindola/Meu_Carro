import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { VeiculoResponse, TipoCombustivel } from '../../models/veiculo.model';
import { AbastecimentoCreateRequest } from '../../models/abastecimento.model';

@Component({
  selector: 'app-abastecimento-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './abastecimento-form.html',
  styleUrl: './abastecimento-form.scss'
})
export class AbastecimentoFormComponent implements OnInit {
  @Input() veiculos: VeiculoResponse[] = [];
  @Input() preselectedVeiculoId?: number;
  @Input() isLoading = signal(false);
  @Output() save = new EventEmitter<AbastecimentoCreateRequest>();
  @Output() cancel = new EventEmitter<void>();

  form!: FormGroup;
  tiposCombustivel = Object.values(TipoCombustivel);

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    const today = new Date().toISOString().split('T')[0];

    this.form = this.fb.group({
      veiculoId: [this.preselectedVeiculoId || '', [Validators.required]],
      data: [today, [Validators.required]],
      tipoCombustivel: [TipoCombustivel.GASOLINA, [Validators.required]],
      valorTotal: [null, [Validators.required, Validators.min(0.01)]],
      litros: [null, [Validators.required, Validators.min(0.01)]],
      quilometragem: [null, [Validators.required, Validators.min(0)]]
    });

    if (this.preselectedVeiculoId) {
      this.form.get('veiculoId')?.disable(); // Optional: Disable if we don't want them to change the preselected one, or just leave it enabled but preselected.
      // But we need the value on submit, so let's just leave it enabled but set.
      this.form.get('veiculoId')?.enable(); 
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      const formValue = this.form.getRawValue();
      // Calculate valorLitro if needed, though optional
      const valorLitro = formValue.valorTotal / formValue.litros;
      
      const request: AbastecimentoCreateRequest = {
        veiculoId: Number(formValue.veiculoId),
        data: formValue.data,
        tipoCombustivel: formValue.tipoCombustivel,
        valorTotal: Number(formValue.valorTotal),
        litros: Number(formValue.litros),
        valorLitro: Number(valorLitro.toFixed(2)),
        quilometragem: Number(formValue.quilometragem)
      };

      this.save.emit(request);
    }
  }

  onCancel(): void {
    this.cancel.emit();
  }
}
