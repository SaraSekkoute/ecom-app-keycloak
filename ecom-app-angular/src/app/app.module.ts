
import { BrowserModule } from '@angular/platform-browser';
import { APP_INITIALIZER, NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProductsComponent } from './ui/products/products.component';
import { CustomersComponent } from './ui/customers/customers.component';
import {  HttpClientModule } from '@angular/common/http';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { OrdersComponent } from './ui/orders/orders.component';
import { OrderDetailsComponent } from './ui/order-details/order-details.component';
function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        // URL du serveur Keycloak (ici, le serveur est local, sur le port 8080)
        url: 'http://localhost:8080',
        // Nom du realm dans Keycloak, qui gère les utilisateurs et permissions
        realm: 'bdcc-realm',
        // Identifiant du client configuré dans Keycloak (correspond à cette application Angular)
        clientId: 'ecom-client-ang'
      },
      initOptions: {
        // Mode de chargement pour vérifier si l'utilisateur est connecté en SSO
        // 'check-sso' : Si l'utilisateur est déjà connecté, il reste connecté automatiquement.
        // Sinon, il doit se reconnecter en accédant à une ressource sécurisée.
        onLoad: 'check-sso',

        // URL utilisée pour vérifier silencieusement si l'utilisateur est encore connecté en SSO
        // Ce fichier doit exister dans le dossier 'assets' de ton projet Angular
        silentCheckSsoRedirectUri:
          window.location.origin + '/assets/silent-check-sso.html'
      }
    });
}


@NgModule({
  declarations: [
    AppComponent,
    ProductsComponent,
    CustomersComponent,
    OrdersComponent,
    OrderDetailsComponent

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
   HttpClientModule, KeycloakAngularModule

  ],
  providers: [
    {
      // Déclare un service spécial qui sera exécuté au démarrage de l'application Angular
      provide: APP_INITIALIZER,
      // Spécifie une fonction (factory) qui sera utilisée pour initialiser Keycloak
      useFactory: initializeKeycloak,
      // Permet d'exécuter ce service en parallèle avec d'autres APP_INITIALIZER (si plusieurs sont définis)
      multi: true,
      // Liste des dépendances nécessaires à la fonction d'initialisation
      // Ici, KeycloakService sera injecté dans initializeKeycloak
      deps: [KeycloakService]
    }
  ],

  bootstrap: [AppComponent]
})
export class AppModule { }
