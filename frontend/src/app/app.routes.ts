import { Routes } from '@angular/router';
import { CLIENT_ROUTES } from '../domain/constants/client-routes';

export const routes: Routes = [
    {
        path: CLIENT_ROUTES.HOME, loadComponent: () => import("./presentation/layouts/default/default")
                                        .then(m => m.Default),
        children: [
            {
                path: '', loadComponent: () => import("./presentation/pages/beneficio-view/beneficio-view")                                 .then(m => m.BenefioView)
            },
            {
                path: `${CLIENT_ROUTES.BENEFICIOS_EDIT}/:id`, loadComponent: () => import("./presentation/pages/beneficio-edit/beneficio-edit")
                                                    .then(m=> m.BenefioEdit)
            },
            {
                path: CLIENT_ROUTES.BENEFICIO_CREATE, loadComponent: () => import("./presentation/pages/beneficio-edit/beneficio-edit")
                                                    .then(m=> m.BenefioEdit)
            },
            {
                path: CLIENT_ROUTES.BENEFICIO_TRANSFER, loadComponent: () => import("./presentation/pages/beneficio-transfer/beneficio-transfer")
                                                    .then(m=> m.BeneficioTransfer)
            }
        ]
    }
];
