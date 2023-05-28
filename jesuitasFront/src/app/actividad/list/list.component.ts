import { Component, OnInit } from '@angular/core';
import { Actividad } from '../actividad';
import { ActividadService } from '../actividad.service';
import { TokenStorageService } from 'src/app/_services/token-storage.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {

  actividades: Actividad[] = [];
  router: any;
  showAdminButton = false;

  constructor(public actividadService: ActividadService, private token: TokenStorageService){}

  ngOnInit(): void {
    this.actividadService.getActividades().subscribe((data: Actividad[])=>{
    this.actividades = data;
    console.log(this.actividades);
  });
  const user = this.token.getUser();
    if (user && user.roles.includes('ROLE_ADMIN')) {
      this.showAdminButton = true;
    }
  }

  deleteActividad(id:number){
    this.actividadService.deleteActividad(id).subscribe(res => {
      this.actividades = this.actividades.filter(item => item.idActividad !== id);
      console.log("Eliminado ok");
      this.router.navigateByUrl('actividad/list');
    })
  }

  updateActividad(id:number): void{
    console.log('actividad', id);
    this.router.navigateByUrl('actividad/edit/:id')
  }

  verActividad(actividad: Actividad): void{
    console.log('actividad', actividad);
    this.router.navigateByUrl('actividad/view')
  }

}
