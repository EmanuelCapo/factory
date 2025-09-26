# Patrón Factory en Spring Boot

---

## Cómo ejecutar la aplicación

###  Requisitos

- Java 21 instalado
- Maven 3.6+ instalado
- IDE como IntelliJ IDEA, VSCode o Eclipse

---

#### Desde un IDE

Ejecutar la clase FactoryApplication.java como aplicación Spring Boot

---

#### Desde línea de comandos

Ejecutar: mvn spring-boot:run

---

La API estará corriendo en: http://localhost:8080

##### Endpoint disponible
POST /api/sales/price

##### Ejemplo de request
{ "country": "CL", "amount": 100.0 }
##### Ejemplo de response
{
"country": "CL",
"baseAmount": 100.0,
"vatRate": 0.19,
"finalAmount": 119.0
}

#### Paises soportados
BR, CL, MX.

---

## Cómo ejecutar los tests


#### Desde línea de comandos

Ejecutar: mvn test

---