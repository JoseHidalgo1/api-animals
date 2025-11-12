# 📧 MENSAJE PARA LOS DESARROLLADORES FRONTEND

## 🚀 NUEVAS FUNCIONALIDADES - API ANIMALS

Hemos agregado **4 nuevos endpoints** al backend para cumplir con los requisitos de consultas personalizadas JPA. A continuación, los detalles de integración:

---

## 📍 1. BÚSQUEDA DE ANIMALES POR PESO MÍNIMO

**Endpoint:** `GET /animals/search/by-weight`

**Parámetro:** `min_weight` (Double) - Peso mínimo en kilogramos

**Descripción:** Retorna todos los animales que tienen un peso mayor o igual al valor especificado, ordenados de mayor a menor peso.

**Ejemplo de llamada:**

```
GET http://localhost:8080/animals/search/by-weight?min_weight=50
```

**Respuesta:**

```json
[
  {
    "id": 102,
    "weight": 1200.0,
    "name": "Elefante",
    "birthDateTime": "2015-03-10T06:30:00",
    "isWild": true
  },
  {
    "id": 101,
    "weight": 190.5,
    "name": "León",
    "birthDateTime": "2018-05-20T08:00:00",
    "isWild": true
  }
]
```

---

## 📍 2. BÚSQUEDA DE ANIMALES POR NOMBRE

**Endpoint:** `GET /animals/search/by-name`

**Parámetro:** `name` (String) - Texto para buscar en el nombre (búsqueda parcial, case-insensitive)

**Descripción:** Retorna todos los animales cuyo nombre contenga el texto especificado.

**Ejemplo de llamada:**

```
GET http://localhost:8080/animals/search/by-name?name=leon
```

**Respuesta:**

```json
[
  {
    "id": 101,
    "weight": 190.5,
    "name": "León",
    "birthDateTime": "2018-05-20T08:00:00",
    "isWild": true
  }
]
```

---

## 📍 3. OBTENER HABITAT CON SUS ANIMALES (MAESTRO-DETALLE)

**Endpoint:** `GET /habitats/{id}/with-animals`

**Parámetro de ruta:** `id` (Integer) - ID del habitat

**Descripción:** Retorna UN habitat específico con la lista completa de todos los animales que pertenecen a ese habitat. **Esta es la consulta MAESTRO-DETALLE principal.**

**Ejemplo de llamada:**

```
GET http://localhost:8080/habitats/1/with-animals
```

**Respuesta:**

```json
{
  "id": 1,
  "name": "Sabana Africana",
  "area": 5000.5,
  "establishedDate": "2020-01-15T10:30:00",
  "isCovered": false,
  "animals": [
    {
      "id": 101,
      "weight": 190.5,
      "name": "León",
      "birthDateTime": "2018-05-20T08:00:00",
      "isWild": true
    },
    {
      "id": 102,
      "weight": 1200.0,
      "name": "Elefante",
      "birthDateTime": "2015-03-10T06:30:00",
      "isWild": true
    },
    {
      "id": 103,
      "weight": 800.0,
      "name": "Jirafa",
      "birthDateTime": "2017-07-22T09:15:00",
      "isWild": true
    }
  ]
}
```

**⚠️ IMPORTANTE:** El objeto `Habitat` ahora incluye un array `animals` con todos los animales asociados. **Deben actualizar su modelo/interfaz de Habitat** para incluir este campo:

**TypeScript/Angular:**

```typescript
export interface Habitat {
  id: number;
  name: string;
  area: number;
  establishedDate: string;
  isCovered: boolean;
  animals?: Animal[]; // ← NUEVO CAMPO
}
```

**Java/Android:**

```java
public class Habitat {
    private Integer id;
    private String name;
    private Double area;
    private String establishedDate;
    private Boolean isCovered;
    private List<Animal> animals;  // ← NUEVO CAMPO
    // ... getters y setters
}
```

---

## 📍 4. OBTENER TODOS LOS HABITATS CON SUS ANIMALES

**Endpoint:** `GET /habitats/with-animals`

**Descripción:** Retorna TODOS los habitats existentes, cada uno con su lista de animales asociados.

**Ejemplo de llamada:**

```
GET http://localhost:8080/habitats/with-animals
```

**Respuesta:**

```json
[
  {
    "id": 1,
    "name": "Sabana Africana",
    "area": 5000.5,
    "establishedDate": "2020-01-15T10:30:00",
    "isCovered": false,
    "animals": [
      { "id": 101, "name": "León", "weight": 190.5, ... },
      { "id": 102, "name": "Elefante", "weight": 1200.0, ... }
    ]
  },
  {
    "id": 2,
    "name": "Acuario Marino",
    "area": 3000.0,
    "establishedDate": "2019-05-10T11:00:00",
    "isCovered": true,
    "animals": [
      { "id": 201, "name": "Delfín", "weight": 150.0, ... },
      { "id": 202, "name": "Tiburón", "weight": 400.0, ... }
    ]
  }
]
```

---

## ⚙️ CAMBIOS EN LOS ENDPOINTS EXISTENTES

Los endpoints existentes **NO han cambiado** su comportamiento:

- ✅ `GET /animals` - Sigue funcionando igual (con filtro `is_wild`)
- ✅ `GET /animals/{id}` - Sigue funcionando igual
- ✅ `GET /habitats` - Sigue funcionando igual (con filtro `is_covered`)
- ✅ `GET /habitats/{id}` - Sigue funcionando igual (pero NO incluye animals)

**Diferencia clave:**

- `GET /habitats/{id}` → Retorna solo el habitat (sin animals)
- `GET /habitats/{id}/with-animals` → Retorna habitat + lista de animals

---

## 🎨 SUGERENCIAS DE UI/UX

### Para búsqueda de animales:

1. **Formulario con dos opciones de búsqueda:**

   - Input numérico para "Peso mínimo (kg)"
   - Input de texto para "Buscar por nombre"
   - Botones independientes o pestañas para cada tipo de búsqueda

2. **Tabla de resultados** mostrando:
   - ID, Nombre, Peso, Fecha de nacimiento, ¿Es salvaje?

### Para Maestro-Detalle (Habitat con Animals):

1. **Vista de detalle expandible:**

   - Card/panel con información del Habitat (maestro)
   - Tabla o lista con los animales de ese habitat (detalles)
   - Contador: "Animales en este habitat: X"

2. **Opción alternativa - Vista de lista:**
   - Lista de todos los habitats
   - Cada habitat muestra su lista de animales colapsable/expandible

---

## 📋 CHECKLIST DE INTEGRACIÓN

- [ ] Actualizar modelo/interfaz de `Habitat` con campo `animals: Animal[]`
- [ ] Crear servicio para `GET /animals/search/by-weight`
- [ ] Crear servicio para `GET /animals/search/by-name`
- [ ] Crear servicio para `GET /habitats/{id}/with-animals`
- [ ] Crear servicio para `GET /habitats/with-animals`
- [ ] Crear componente/pantalla de búsqueda de animales
- [ ] Crear componente/pantalla de detalle de habitat (maestro-detalle)
- [ ] Probar endpoints con datos reales
- [ ] Validar manejo de casos sin resultados (arrays vacíos)

---

## 🧪 DATOS DE PRUEBA

Para facilitar las pruebas, asegúrense de tener:

- Al menos 2 habitats creados
- Al menos 2-3 animales asociados a cada habitat
- Animales con diferentes pesos para probar el filtro por peso mínimo

---

## 🆘 SOPORTE

Si tienen dudas o encuentran algún problema con estos endpoints, contacten conmigo. Los endpoints están completamente funcionales y probados.

**Fecha de implementación:** Noviembre 9, 2025
**Backend version:** 1.1.0

---

## 📎 QUICK REFERENCE

```
Nuevos Endpoints:
1. GET /animals/search/by-weight?min_weight={valor}
2. GET /animals/search/by-name?name={texto}
3. GET /habitats/{id}/with-animals
4. GET /habitats/with-animals

Cambio en modelo:
- Habitat ahora incluye: animals?: Animal[]
```

---

## 💡 EJEMPLO DE CÓDIGO PARA INTEGRACIÓN

### Angular/TypeScript

**1. Actualizar el servicio de Animals (`animal.service.ts`):**

```typescript
// Buscar animales por peso mínimo
searchByMinWeight(minWeight: number): Observable<Animal[]> {
  return this.http.get<Animal[]>(`${this.apiUrl}/search/by-weight?min_weight=${minWeight}`);
}

// Buscar animales por nombre
searchByName(name: string): Observable<Animal[]> {
  return this.http.get<Animal[]>(`${this.apiUrl}/search/by-name?name=${name}`);
}
```

**2. Actualizar el servicio de Habitats (`habitat.service.ts`):**

```typescript
// Obtener habitat con sus animales (Maestro-Detalle)
getHabitatWithAnimals(id: number): Observable<Habitat> {
  return this.http.get<Habitat>(`${this.apiUrl}/${id}/with-animals`);
}

// Obtener todos los habitats con sus animales
getAllHabitatsWithAnimals(): Observable<Habitat[]> {
  return this.http.get<Habitat[]>(`${this.apiUrl}/with-animals`);
}
```

**3. Componente de búsqueda (`animal-search.component.ts`):**

```typescript
export class AnimalSearchComponent {
  animals: Animal[] = [];
  minWeight: number = 0;
  searchName: string = "";

  constructor(private animalService: AnimalService) {}

  searchByWeight(): void {
    this.animalService.searchByMinWeight(this.minWeight).subscribe({
      next: (data) => (this.animals = data),
      error: (err) => console.error("Error:", err),
    });
  }

  searchByName(): void {
    this.animalService.searchByName(this.searchName).subscribe({
      next: (data) => (this.animals = data),
      error: (err) => console.error("Error:", err),
    });
  }
}
```

**4. Componente Maestro-Detalle (`habitat-detail.component.ts`):**

```typescript
export class HabitatDetailComponent implements OnInit {
  habitat: Habitat | null = null;

  constructor(
    private route: ActivatedRoute,
    private habitatService: HabitatService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get("id"));
    this.habitatService.getHabitatWithAnimals(id).subscribe({
      next: (data) => (this.habitat = data),
      error: (err) => console.error("Error:", err),
    });
  }
}
```

---

### Java/Android

**1. Actualizar el servicio de Animals (Retrofit):**

```java
public interface AnimalService {
    // Buscar animales por peso mínimo
    @GET("animals/search/by-weight")
    Call<List<Animal>> searchByMinWeight(@Query("min_weight") Double minWeight);

    // Buscar animales por nombre
    @GET("animals/search/by-name")
    Call<List<Animal>> searchByName(@Query("name") String name);
}
```

**2. Actualizar el servicio de Habitats (Retrofit):**

```java
public interface HabitatService {
    // Obtener habitat con sus animales (Maestro-Detalle)
    @GET("habitats/{id}/with-animals")
    Call<Habitat> getHabitatWithAnimals(@Path("id") Integer id);

    // Obtener todos los habitats con sus animales
    @GET("habitats/with-animals")
    Call<List<Habitat>> getAllHabitatsWithAnimals();
}
```

**3. Modelo Habitat actualizado:**

```java
public class Habitat {
    private Integer id;
    private String name;
    private Double area;
    private String establishedDate;
    private Boolean isCovered;
    private List<Animal> animals; // ← NUEVO

    // Getters y Setters
    public List<Animal> getAnimals() { return animals; }
    public void setAnimals(List<Animal> animals) { this.animals = animals; }
}
```

---

## ✅ VALIDACIONES A CONSIDERAR

1. **Búsqueda por peso:**

   - Validar que `min_weight` sea un número positivo
   - Mostrar mensaje si no hay resultados

2. **Búsqueda por nombre:**

   - Validar que el texto no esté vacío
   - Mostrar mensaje si no hay resultados

3. **Vista Maestro-Detalle:**
   - Manejar el caso cuando un habitat no tiene animales (array vacío)
   - Mostrar contador de animales

---

## 🎯 CRITERIOS DE ACEPTACIÓN

Para considerar la integración completa, deben:

1. ✅ Poder buscar animales por peso mínimo y mostrar resultados
2. ✅ Poder buscar animales por nombre y mostrar resultados
3. ✅ Poder visualizar un habitat con todos sus animales (vista maestro-detalle)
4. ✅ Manejar correctamente los casos sin resultados
5. ✅ Validar inputs antes de hacer las llamadas al API
