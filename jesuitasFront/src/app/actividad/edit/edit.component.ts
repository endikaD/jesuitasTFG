import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Actividad } from '../actividad';
import { ActividadService } from '../actividad.service';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit {

  idActividad!:number;
  nuevoActividad!: Actividad;
  form!: FormGroup;

  constructor(private actividadService: ActividadService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    this.idActividad = this.route.snapshot.params['idActividad'];
    this.actividadService.getActividad(this.idActividad).subscribe((data: Actividad)=>{
      this.nuevoActividad = data;
    });

    this.form = new FormGroup({
      titulo: new FormControl('', [Validators.required]),
      descripcion: new FormControl('', [Validators.required]),
      fecha: new FormControl('', [Validators.required]),
      hora: new FormControl('', [Validators.required]),
      etapa: new FormControl('', [Validators.required]),
      participantesMax: new FormControl('', [Validators.required])
    });
  }

  get f(){
    return this.form.controls;
  }

  submit(){
    console.log(this.form.value);
    this.actividadService.updateActividad(this.idActividad, this.form.value).subscribe((res:any)=> {
      console.log('Actividad actualizada');
      this.router.navigateByUrl('actividad/list');
    })
  }

}
