import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TipoManutencao, PlanoManutencaoResponse, PlanoManutencaoCreateRequest } from '../../models/plano-manutencao.model';

@Component({
  selector: 'app-plano-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './plano-form.html',
  styleUrl: './plano-form.scss'
})
export class PlanoFormComponent implements OnInit {
  @Input() plano?: PlanoManutencaoResponse;
  @Input() tipo?: TipoManutencao;
  @Input() isLoading = signal(false);
  @Output() save = new EventEmitter<PlanoManutencaoCreateRequest>();
  @Output() cancel = new EventEmitter<void>();

  form!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      tipo: [this.plano?.tipo || this.tipo || TipoManutencao.OUTRO, [Validators.required]],
      descricao: [this.plano?.descricao || ''],
      intervaloKm: [this.plano?.intervaloKm || null, [Validators.min(0)]],
      intervaloDias: [this.plano?.intervaloDias || null, [Validators.min(0)]],
      ativo: [this.plano?.ativo ?? true]
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
