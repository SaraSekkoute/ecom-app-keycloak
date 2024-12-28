import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Router,
  RouterStateSnapshot
} from '@angular/router';
import { KeycloakAuthGuard, KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard extends KeycloakAuthGuard {
  constructor(
    protected readonly router_: Router,
    protected readonly keycloak: KeycloakService
  ) {
    super(router_, keycloak);
  }
//si autoriser ou pas
  public async isAccessAllowed(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ) {
    // Force the user to log in if currently unauthenticated.
    if (!this.authenticated) {
      await this.keycloak.login({
        redirectUri: window.location.origin + state.url
      });
    }

    // Get the roles required from the route.
    //const requiredRoles = route.data.roles;
    const requiredRoles: string[] = route.data['roles'] ;

    // Allow the user to proceed if no additional roles are required to access the route.
    if (!Array.isArray(requiredRoles) || requiredRoles.length === 0) {
      return true;
    }

    // Allow the user to proceed if all the required roles are present.
    // Ici, `this.roles` représente les rôles de l'utilisateur connecté ( stockés dans un service d'authentification) si il est parmi les role

    return requiredRoles.every((role) => this.roles.includes(role));
  }
}
