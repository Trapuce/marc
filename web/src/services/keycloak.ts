import Keycloak from 'keycloak-js'
const keycloakInstance = new Keycloak('/keycloak.json')

const Init = (): Promise<boolean> => {
  return keycloakInstance
    .init({ onLoad: 'check-sso' })
    .then((authenticated) => {
      if (authenticated) {
        localStorage.setItem('token', keycloakInstance.token || '')
      }

      return authenticated
    })
    .catch((error) => {
      console.error('Keycloak init exception:', error)
      return false
    })
}


const Roles = () => {
  if (keycloakInstance.authenticated) {
    const clientId = "marcapp";
    return keycloakInstance.tokenParsed?.resource_access?.[clientId]?.roles || [];
  }
  return [];
}


const Login = () => keycloakInstance.login({ redirectUri: window.location.origin + '/loading' })

const UserName = (): string | undefined => keycloakInstance?.tokenParsed?.preferred_username

const Token = (): string | undefined => keycloakInstance?.token

const LogOut = () => {
  localStorage.removeItem('token')
  keycloakInstance.logout({ redirectUri: window.location.origin + '/auth/logout' })
}

const UpdateToken = (successCallback: any) =>
  keycloakInstance.updateToken(5).then(successCallback).catch(DoLogin)

const DoLogin = keycloakInstance.login

const IsLoggedIn = () => !!keycloakInstance.authenticated

const KeyCloakService = {
  Init: Init,
  Login: Login,
  GetUserName: UserName,
  GetAccessToken: Token,
  GetRoles : Roles,
  CallLogOut: LogOut,
  UpdateToken: UpdateToken,
  IsLoggedIn: IsLoggedIn
}

export default KeyCloakService
