import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ManutencaoResponse, ManutencaoCreateRequest } from '../../models/manutencao.model';
import { PlanoManutencaoResponse } from '../../models/plano-manutencao.model';

@Component({
  selector: 'app-manutencao-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './manutencao-form.html',
  styleUrl: './manutencao-form.scss'
})
export class ManutencaoFormComponent implements OnInit {
  @Input() manutencao?: ManutencaoResponse;
  @Input() planos: PlanoManutencaoResponse[] = [];
  @Input() isLoading = signal(false);
  @Output() save = new EventEmitter<ManutencaoCreateRequest>();
  @Output() cancel = new EventEmitter<void>();

  form!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    const today = new Date().toISOString().split('T')[0];
    
    this.form = this.fb.group({
      planoId: [this.manutencao?.planoId || null],
      descricao: [this.manutencao?.descricao || '', [Validators.required]],
      data: [this.manutencao?.data || today, [Validators.required]],
      quilometragem: [this.manutencao?.quilometragem || 0, [Validators.required, Validators.min(0)]],
      valor: [this.manutencao?.valor || 0, [Validators.required, Validators.min(0)]]
    });

    // If a plan is selected, we could potentially auto-fill the description
    this.form.get('planoId')?.valueChanges.subscribe(planoId => {
      if (planoId && !this.form.get('descricao')?.value) {
        const selectedPlano = this.planos.find(p => p.id === Number(planoId));
        if (selectedPlano) {
          this.form.get('descricao')?.setValue(selectedPlano.tipo);
        }
      }
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.save.emit(this.form.value);
    }
  }

  onCancel(): void {
    this.cancel.emit();
  }
}
