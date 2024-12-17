# Voluntariado
 
Link al repositorio: https://github.com/siraglez/Voluntariado.git

# Aplicación de Voluntariado

Este proyecto es una aplicación para gestionar actividades de voluntariado, con roles diferenciados de **administrador** y **usuario**.

---

## Paquete: `auth`
Este paquete maneja el flujo de autenticación y registro de usuarios.

### **LoginActivity**
- Es la actividad encargada de la autenticación.
- Los usuarios ingresan su **email** y **contraseña**.
- Verifica las credenciales en **Firestore** y redirige al usuario a `MainActivity` según su **rol** (admin/usuario).

### **RegistroActivity**
- Permite a los usuarios registrarse.
- Los administradores son asignados manualmente con el email `admin`.
- Guarda la información del usuario en Firestore, como nombre, ubicación, contraseña e intereses.

---

## Paquete: `data`
Contiene clases auxiliares para interactuar con la base de datos **Firestore**.

### **FirebaseHelper**
- Proporciona funciones para realizar operaciones CRUD:
  - **Usuarios**: Agregar y obtener información.
  - **Actividades**: Crear actividades con IDs secuenciales, listar y obtener detalles.
  - **Inscripciones**: Permite inscribir a usuarios en actividades.
- Implementa **transacciones** para garantizar que no se excedan los límites de voluntarios.

---

## Paquete: `model`
Contiene las clases de modelo para representar las entidades del sistema.

### **Usuario**
- Representa un usuario registrado.
- Atributos: UID, nombre, email, ubicación, contraseña, rol e intereses.

### **Actividad**
- Representa una actividad de voluntariado.
- Atributos: ID, nombre, descripción, fecha, ubicación, voluntarios máximos y actuales, estado e inscritos.

### **Inscripcion**
- Representa la inscripción de un usuario a una actividad.
- Atributos: ID de usuario, ID de actividad y fecha de inscripción.

---

## Paquete: `adapter`
Contiene adaptadores para manejar listas en la interfaz de usuario.

### **ActividadAdapter**
- Adaptador para mostrar actividades en un `RecyclerView`.
- Muestra el nombre y la descripción de cada actividad.
- Proporciona la opción de hacer clic en una actividad para ver más detalles.

---

## Paquete: `view`
Contiene las actividades y fragmentos de la interfaz de usuario.

### **MainActivity**
- Es la actividad principal.
- Muestra un listado de actividades usando el `ActividadVoluntariadoFragment`.
- Los administradores tienen acceso a un botón para **agregar actividades**.

### **ActividadVoluntariadoFragment**
- Fragmento que lista las actividades en tiempo real.
- Administra la visualización de las actividades según el rol:
  - **Admin**: Puede ver la lista de inscritos en cada actividad.
  - **Usuario**: Puede inscribirse en actividades con espacio disponible.

### **AgregarActividadActivity**
- Permite a los administradores agregar nuevas actividades.
- Genera automáticamente un ID secuencial para cada actividad.

### **DetallesActividadActivity**
- Muestra los detalles completos de una actividad.
- Los administradores ven la lista de usuarios inscritos.
- Los usuarios pueden inscribirse si hay espacio disponible.

---

## Flujo de la Aplicación
1. **Inicio de sesión** o **registro** en `LoginActivity` o `RegistroActivity`.
2. Los usuarios autenticados son redirigidos a `MainActivity`, donde se cargan las actividades.
3. Los administradores pueden:
   - Agregar nuevas actividades.
   - Ver la lista de inscritos en una actividad.
4. Los usuarios normales pueden:
   - Ver detalles de una actividad.
   - Inscribirse en actividades si hay cupo disponible.

---

## Base de Datos
- Se utiliza **Firebase Firestore** para almacenar:
  - Usuarios.
  - Actividades.
  - Inscripciones.

---

## Rol de Usuario
- **Administrador**: Gestiona actividades y visualiza usuarios inscritos.
- **Usuario**: Visualiza e interactúa con actividades.
