# Informe detallado de los últimos cambios de login

## Objetivo general
Implementar un flujo de login **muy básico** que permita al frontend validar usuarios ya existentes en el backend sin introducir capas adicionales de seguridad, sesiones ni tokens. El foco fue ofrecer un punto de integración mínimo para las pruebas manuales del formulario.

## Cambios en el backend (modulo `user-solution`)
1. **DTO de entrada (`LoginRequest`)**
   - Ubicación: `user-solution/src/main/java/edu/uoc/epcsd/user/application/rest/request/LoginRequest.java`
   - Campos: `email` y `password`, ambos marcados con `@NotBlank` para rechazar peticiones vacías a nivel de validación.
   - Motivo: disponer de un contrato explícito y sencillo que refleje los datos mínimos que envía el frontend.

2. **DTO de salida (`LoginResponse`)**
   - Ubicación: `user-solution/src/main/java/edu/uoc/epcsd/user/application/rest/response/LoginResponse.java`
   - Campos devueltos: `id`, `fullName`, `email`, `phoneNumber`.
   - Motivo: regresar un perfil ligero del usuario autenticado sin exponer la contraseña ni datos innecesarios.

3. **Servicio de autenticación (`UserService` / `UserServiceImpl`)**
   - Se añadió el método `Optional<User> authenticate(String email, String password)` en la interfaz y su implementación.
   - Lógica: buscar por email y filtrar si la contraseña coincide exactamente con la almacenada en memoria.
   - Motivo: reutilizar el repositorio existente para comprobar credenciales sin añadir cifrado ni persistencia adicional, acorde al requerimiento de sencillez.

4. **Endpoint REST `/users/login`**
   - Ubicación: `user-solution/src/main/java/edu/uoc/epcsd/user/application/rest/UserRESTController.java`.
   - Comportamiento: recibe `LoginRequest`, delega en `authenticate` y devuelve `200 OK` con `LoginResponse` o `401 Unauthorized` si no coincide.
   - Motivo: exponer un punto de entrada claro para el formulario del frontend, manteniendo la semántica HTTP estándar de éxito o credenciales inválidas.

## Cambios en el frontend (modulo `frontend-solution`)
1. **Cliente de usuario (`src/services/user/api.js`)**
   - Configura un cliente Axios apuntando a `process.env.VUE_USER_API_URL` (por defecto `http://localhost:18080`).
   - Expone `login(credentials)` que envía un POST a `/users/login` con email y contraseña.
   - Motivo: centralizar la llamada y permitir reconfigurar la URL del backend sin tocar componentes.

2. **Componente de login (`src/components/UserLogin.vue`)**
   - Formulario con campos controlados `email` y `password`, estilos de Bootstrap y mensajes dinámicos.
   - Lógica: invoca `api.login`, muestra mensaje de bienvenida con el `fullName` retornado o un mensaje de error si recibe `401`.
   - Motivo: ofrecer una interfaz mínima para probar el endpoint y visualizar rápidamente si las credenciales son válidas.

3. **Integración en la aplicación (`src/App.vue`)**
   - Se importa y renderiza `<UserLogin />` junto al formulario de productos existente.
   - Motivo: disponer del login en la pantalla principal para facilitar la validación manual sin navegación adicional.

## Flujo completo de autenticación
1. El usuario rellena email y contraseña en `UserLogin.vue`.
2. El componente llama a `api.login`, que envía la petición al backend de usuarios.
3. `UserRESTController` recibe la petición, valida campos y delega en `UserService.authenticate`.
4. `UserServiceImpl` busca el usuario por email y comprueba si la contraseña coincide exactamente.
5. Si coincide, se responde con `LoginResponse` (id, nombre, email, teléfono); si no, se responde `401` sin cuerpo.
6. El frontend muestra un mensaje verde de bienvenida o rojo de credenciales incorrectas según el resultado.

## Supuestos y limitaciones conscientes
- No se implementó cifrado de contraseñas ni tokens; las contraseñas se comparan en claro porque se pidió un login “sin complicaciones”.
- No hay sesiones ni almacenamiento de estado en frontend; solo se muestra el resultado de la autenticación.
- El catálogo de usuarios proviene del repositorio en memoria ya presente en el proyecto; no se añadió creación ni semillado extra.

## Razones detrás de las decisiones
- **Simplicidad operativa:** al reutilizar los repositorios existentes y evitar dependencias nuevas, el endpoint funciona con la configuración actual del proyecto.
- **Contratos explícitos:** los DTOs de entrada/salida documentan qué espera y qué ofrece el login, facilitando pruebas y depuración.
- **Retroalimentación clara en UI:** el componente muestra mensajes directos (éxito/error) para validar manualmente sin herramientas externas.

## Pruebas realizadas
- Se intentó ejecutar `mvn -q -f user-solution/pom.xml test`, pero falló por un error 403 al descargar el parent POM desde Maven Central (limitación del entorno).
- Las pruebas manuales se basan en llamadas desde el formulario al endpoint; no se añadieron tests automatizados específicos para el login.

## Próximos pasos sugeridos
- Sustituir la comparación de contraseñas en claro por hashing (e.g., BCrypt) y almacenamiento cifrado.
- Emitir tokens o sesiones para mantener al usuario autenticado en posteriores peticiones.
- Añadir validaciones de formato de email y reglas de contraseña más estrictas en frontend y backend.
- Incorporar pruebas unitarias para `authenticate` y pruebas de integración para el endpoint `/users/login`.
