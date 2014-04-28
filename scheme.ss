(define PI 3.14159265359)

(define read-objects
  (lambda (in)
    (let ([object (read in)])
      (cond 
        ((eof-object? object) '())
        (else (cons object (read-objects in)))))))

(define read-file
  (lambda (path)
    (read-objects (open-input-file path))))

; Recibe: n = Número al que se quiere saber el factorial.
; Retorna: El factorial del número ingresado.
; Ejemplo: (factorial 5) => 120.
(define factorial
  (lambda (n)
    (cond
      ((= n 0) 1)
      (else (* n (factorial (- n 1)))))))

; Recibe: n = Cantidad de elementos de un conjunto.
;         k = Número de elementos a escoger.
; Retorna: El número de formas en que se pueden extraer k elementos de  un conjunto de cardinalidad n.
; Ejemplo: (combinacion 10 3) => 120.
(define combinacion
  (lambda (n k)
    (/ (factorial n) (* (factorial k) (factorial (- n k))))))

; Función que genera una tabla acumulada a partir de resulatados de distribuciones.
; Recibe; l1 = Lista de valores con sus probabilidades. 
;         l2 = Lista nueva con los valores acumulados.
; Retorna: La acumulada de una distribución.
(define acumulada-aux
  (lambda (l1 l2)
    (cond
      ((null? l1) l2)
      ((null? l2) (acumulada-aux (cdr l1) (append l2 (list (car l1)))))
      (else (acumulada-aux (cdr l1) (append l2 (list (list (caar l1) (+ (cadar l1) (second (last l2)))))))))))


; Función que genera una tabla acumulada a partir de resulatados de distribuciones.
; Recibe; l1 = Lista de valores con sus probabilidades.
; Retorna: La acumulada de una distribución.
(define acumulada
  (lambda (l)
    (acumulada-aux l '())))

; Función que busca en una tabla o lista de valores el valor más cercano al buscado.
; Recibe: x = Valor a buscar.
;         l = Lista donde se va a buscar el valor.
(define buscar-en-tabla
  (lambda (x l)
    (cond
      ((null? l) +nan.0)
      ((<= x (cadar l)) (caar l))
      (else (buscar-en-tabla x (cdr l))))))

; Recibe: rango = Rango de la tabla a generar.
;         fun = Una función para aplicarle a cada numero en el rango.
; Retorna: Una lista que contiene un par de tipo (x (fun x)) donde x es cada elemento del rango.
; Ejemplo: (generar-tabla-discreta '(0 3) (lambda (n) (* n 2))) => '((0 0) (1 2) (2 4) (3 6)).
(define generar-tabla-discreta
  (lambda (rango fun)
    (cond
      ((> (first rango) (last rango)) '())
      (else (append (list (list (first rango) (fun (first rango)))) (generar-tabla-discreta (list (+ (first rango) 1) (last rango)) fun))))))

; Recibe: l = Una lista de listas con el siguiente formato: '((x1 Px1)(x2 Px2)....(xn Pxn)).
;            Donde xn es el número y Pxn es la probabilidad del número.
; Retorna: Una función λ que dado un valor [0,1[ devuelva un valor xi que siga la distribución según la tabla.
(define tabla
  (lambda (l)
    (lambda (k)
      (buscar-en-tabla k (acumulada l)))))

;Recibe: n = El tamaño de la cantidad de experimentos.
;        p = Probabilidad de éxito.
;Retorna: Una función λ que dado un valor k retorne la probabilidad de ser escogido.
;Ejemplo: (binomial 15 0.3).
(define binomial
  (lambda (n p)
    (lambda (k)
      (* (combinacion n k) (expt p k) (expt (- 1 p) (- n k))))))

; Recibe: N = Tamaño de la población.
;         d = Cantidad de los “exitosos”.
;         n = Muestra seleccionada.
; Retorna: Una función λ que dado un valor k retorne la probabilidad de ser escogido.
; Ejemplo: (hipergeometrica 100 15 30).
(define hipergeometrica
  (lambda (N d n)
    (lambda (k)
      (/ (* (combinacion d k) (combinacion (- N d) (- n k))) (combinacion N n)))))

; Recibe: l = Una lista de valores.
; Retorna: Una función λ que retorne de manera uniforme alguno de los valores de la lista.
; Ejemplo: (uniforme-disc '(2 4 6 8)).
(define uniforme-disc
  (lambda (l)
    (lambda ()
      (list-ref l (random (length l))))))

(define uniforme
  (lambda (ini fin)
    (lambda ()
      (+ ini (random (- fin ini)) (random)))))

;Recibe: p = Una probabilidad de exito p con 0 <= p <= 1.
;        n = Rango para la función acumulada.
;Retorna: Una función λ que dado un valor k retorne la probabilidad de ser escogido.
;Ejemplo: (geometrica 0.3).
(define geometrica
  (lambda (n p)
    (lambda (k)
      (* (expt (- 1 p) k) p))))

;Recibe: m = Valor de la media.
;Retorna: Una función λ que dado un valor k retorne la probabilidad de ser escogido.
;Ejemplo: (poisson 3).
(define poisson
  (lambda (m)
    (lambda (k)
      (/ (* (expt m k) (exp (* m -1))) (factorial k)))))

; Recibe: u = Valor para su media.
; Retorna: Una función λ que dado un valor [0,1[ retorne un valor exponencial.
; Ejemplo: (exponencial 3).
(define exponencial
  (lambda (u)
    (lambda (k)
      (cond
        ((>= k 0) (* u (exp (* (* -1 u) k))))
        (else 0)))))

; Recibe: m = Media.
;         s = Desviación estándar.
; Retorna: Una función λ que dado un valor [0,1[ retorne un valor con una probabilidad normal.
(define normal
  (lambda (m s)
    (lambda (k)
      (* 
       (/ 1 (* s (sqrt (* 2 PI))))
       (exp (* -1/2 (cuad (/ (- k m) s))))))))

(define normal-estandar (normal 0 1))

; Función que se encarga de eviar los datos al servidor de las funciones de tipo deiscreta.
; Recibe: i = Contador de datos enviados.
;         n = Cantidad de datos a enviar.
;         table = Lista donde se van a buscar los valores a enviar.
;         out = Enviar información por el puerto.
(define send-data-discrete
  (lambda (i n table out)
    (cond 
      ((< i n)
       (displayln (buscar-en-tabla (random) table) out)
       (send-data-discrete (+ i 1) n table out)))))

; Función que se encarga de eviar los datos al servidor de las funciones de tipo continuas.
; Recibe: i = Contador de datos enviados.
;         n = Cantidad de datos a enviar.
;         func = Función a aplicar a los datos.
;         out = Enviar información por el puerto.
(define send-data-continue
  (lambda (i n func out)
    (cond
      ((< i n)
       (displayln "Not implemented.")
       (send-data-continue n n func out)))))

; Función que se encarga de eviar los datos al servidor de las funciones de tipo uniformes.
; Recibe: i = Contador de datos enviados.
;         n = Cantidad de datos a enviar.
;         func = Función a aplicar a los datos.
;         out = Enviar información por el puerto.
(define send-data-uniform
  (lambda (i n func out)
    (cond 
      ((< i n) 
       (displayln (func) out)
       (send-data-uniform (+ i 1) n func out)))))

; Función que se encarga de controlar las funciones discretas.
; Recibe: args = Contiene la información necesaria para las funciones.
;         out = Enviar información por el puerto.
(define handle-discrete
  (lambda (args out)
    (send-data-discrete 0 (first args) (acumulada (generar-tabla-discreta (third args) (eval (last args)))) out)))

; Función que se encarga de controlar las funciones continuas.
; Recibe: args = Contiene la información necesaria para las funciones.
;         out = Enviar información por el puerto.
(define handle-continue
  (lambda (args out)
    (send-data-continue 0 (first args) (eval (last args)) out)))

; Función que se encarga de controlar las funciones uniformes.
; Recibe: args = Contiene la información necesaria para las funciones.
;         out = Enviar información por el puerto.
(define handle-uniform
  (lambda (args out)
    (send-data-uniform 0 (first args) (eval (last args)) out)))


; Función que sedefine que tipo de función se quiere ejecutar.
; Recibe: args = Contiene la información del archivo necesaria para la ejecución.
;         out = Enviar información por el puerto.
(define function-switch
  (lambda (args out)
    (case (second args)
      ('discreta (handle-discrete args out))
      ('continua (handle-continue args out))
      ('uniforme (handle-uniform args out)))))

; Función que inicia la conexión con el servidor, y la ejecución de las funciones dependiendo de la información que se lee del archivo.
; Recibe: path = Dirección donde se encuentra el archivo a leer.
;         host = Dirección ip o nombre del servidor al que se va a conectar.
;         port: Número del puerto por el cuál se va a comunicar con el servidor.
(define start 
  (lambda (path host port)
    (with-handlers
        ([exn:fail:network? (lambda (e) (displayln "Failed To Connect To Socket."))]
         [exn:fail:filesystem? (lambda (e) (displayln "Failed To Open File."))])
      (let-values ([(in out) (tcp-connect host port)])
        (function-switch (read-file path) out)
        (close-output-port out)))))

; Realiza el método compuesto de Simpson.
; Divide cada unidad en 1000 partes iguales.

; Recibe: func = Función sobre la cual se va a calcular  la inegral.
;         a = Rango inicial de la integral.
;         b = Final del rango de la integral.
; Retonra: Valor de la integral más un error que se basa en la cuarta derivación de la función.
(define simpson
  (lambda (func a b)
    (* (/ (/ 1000) 3) (simpson-aux func a b (* (- b a) 1000)))))

; Recibe: func = Función sobre la cual se va a calcular  la inegral.
;         a = Rango inicial de la integral.
;         b = Final del rango de la integral.
;         particiones: Cantidad de particiones en que se va a dividir la unidad.
; Retonra: Valor de la integral más un error que se basa en la cuarta derivación de la función.
(define simpson-aux
  (lambda (func a b particiones)
    (+ (func a) (func b) (suma-simpson func #f (+ a (/ (- b a) particiones)) b (/ (- b a) particiones) 0))))

; Recibe: func = Función que se va a aplicar.
;         es-par = Valor booleano que indica si un número es par.
;         val-act = Valor actual(contador).
;         tope =  Tope de la suma.
;         aumento = Valor en que se aumenta el contador.
;         acum = Valor acumulado de la suma.
(define suma-simpson
  (lambda (func es-par val-act tope aumento acum)
    (cond ((>= val-act tope) acum)
          (es-par
           (suma-simpson
            func (not es-par) (+ val-act aumento) tope aumento (+ (* 2 (func val-act)) acum)))
           (else
            (suma-simpson
             func (not es-par) (+ val-act aumento) tope aumento (+ (* 4 (func val-act)) acum))))))
            

(start "archivo1.txt" "localhost" 2020)