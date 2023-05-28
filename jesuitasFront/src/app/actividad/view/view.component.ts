import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Actividad } from '../actividad';
import { ActividadService } from '../actividad.service';

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.css']
})
export class ViewComponent implements OnInit {

  id!: number;
  actividad!: Actividad;

  constructor(
    public actividadService: ActividadService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['idActividad'];

    this.actividadService.getActividad(this.id).subscribe((data: Actividad) =>{
      this.actividad = data;
    });
  }
}
