import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ActividadService } from '../actividad.service';

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css']
})
export class AddComponent implements OnInit {

  form!: FormGroup;

  constructor(public actividadService: ActividadService,private router: Router, private formBuilder: FormBuilder) {
    this.form = this.formBuilder.group({});
  }

  ngOnInit(): void {
    this.form = new FormGroup({
      titulo: new FormControl('', [Validators.required]),
      descripcion: new FormControl('', [Validators.required]),
      fecha: new FormControl('', [Validators.required]),
      hora: new FormControl('', [Validators.required]),
      etapa: new FormControl('', [Validators.required]),
      participantesMax: new FormControl('', [Validators.required]),
      imageSrc: new FormControl('', [Validators.required])
    });
  }

  get f(){
    return this.form.controls;
  }
  submit(){
    console.log(this.form.value);
    this.actividadService.addActividad(this.form.value).subscribe((res:any)=> {
      console.log('Actividad añadida');
      this.router.navigateByUrl('actividad/list');
    })
  }

}
