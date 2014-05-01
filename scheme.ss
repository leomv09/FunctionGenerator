(define PI 3.141592653589793)

; Recibe: in = Un puerto de lectura.
; Retorna: Una lista con todos los objetos leídos del puerto hasta que se lea un caracter EOF.
(define read-objects
  (lambda (in)
    (let ([object (read in)])
      (cond 
        ((eof-object? object) '())
        (else (cons object (read-objects in)))))))

; Recibe: path = Ruta del archivo.
; Retorna: Una lista con cada objeto del archivo.
(define read-file
  (lambda (path)
    (read-objects (open-input-file path))))

; Recibe: n = Número natural.
; Retorna: El factorial del número ingresado.
; Ejemplo: (factorial 5) => 120.
(define factorial
  (lambda (n)
    (cond
      ((= n 0) 1)
      (else (* n (factorial (- n 1)))))))

(define cuad
  (lambda (n)
    (* n n)))

; Recibe: n = Cantidad de elementos de un conjunto.
;         k = Número de elementos a escoger.
; Retorna: El número de formas en que se pueden extraer k elementos de un conjunto de cardinalidad n.
; Ejemplo: (combinacion 10 3) => 120.
(define combinacion
  (lambda (n k)
    (/ (factorial n) (* (factorial k) (factorial (- n k))))))

; Realiza el método compuesto de Simpson.
; Recibe: func = Función sobre la cual se va a calcular la integral.
;         a = Inicio del rango la integral.
;         b = Final del rango de la integral.
; Retorna: Valor de la integral.
(define simpson
  (lambda (func a b)
    (* (/ (/ 1000) 3) (simpson-aux func a b (* (- b a) 1000)))))

; Recibe: func = Función sobre la cual se va a calcular  la inegral.
;         a = Inicio del rango la integral..
;         b = Final del rango de la integral.
;         particiones: Cantidad de particiones en que se va a dividir la unidad.
; Retorna: Valor de la integral más un error que se basa en la cuarta derivación de la función.
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

; Función que genera una tabla acumulada a partir de una tabla de distribución.
; Recibe; table = Tabla de distribución. 
;         acum = Tabla de valores acumulados.
; Retorna: La tabla acumulada de una distribución.
(define acumulada-aux
  (lambda (table acum)
    (cond
      ((null? table) acum)
      ((null? acum) (acumulada-aux (cdr table) (append acum (list (car table)))))
      (else (acumulada-aux (cdr table) (append acum (list (list (caar table) (+ (cadar table) (second (last acum)))))))))))

; Función que genera una tabla acumulada a partir de una tabla de distribución.
; Recibe; table = Tabla de distribución.
; Retorna: La tabla acumulada de una distribución.
(define acumulada
  (lambda (table)
    (acumulada-aux table '())))

; Función que busca una probabilidad en una tabla acumulada de distribución.
; Recibe: x = Valor de la probabilidad.
;         table = Tabla acumulada de distribución.
; Retorna: El primer valor K tal que la probabilidad de K sea mayor o igual a X.
(define buscar-en-tabla
  (lambda (x table)
    (cond
      ((null? table) +nan.0)
      ((<= x (cadar table)) (caar table))
      (else (buscar-en-tabla x (cdr table))))))

; Recibe: rango = Rango de la tabla a generar.
;         fun = Una función para aplicarle a cada numero en el rango.
; Retorna: Una tabla que contiene pares de tipo (x (fun x)) donde x es cada elemento del rango.
; Ejemplo: (generar-tabla-discreta '(0 3) (lambda (n) (* n 2))) => '((0 0) (1 2) (2 4) (3 6)).
(define generar-tabla-discreta
  (lambda (rango fun)
    (cond
      ((> (first rango) (last rango)) '())
      (else (append (list (list (first rango) (fun (first rango)))) (generar-tabla-discreta (list (+ (first rango) 1) (last rango)) fun))))))

(define generar-tabla-continua
  (lambda (rango func)
    (cond ((<= (first rango) (second rango))
           (append (list (list (first rango) (func (- (first rango) (last rango)) (first rango)))) (generar-tabla-continua (list (+ (first rango) (last rango)) (second rango) (last rango)) func)))
           (else '()))))

; Recibe: l = Una lista de listas con el siguiente formato: '((x1 Px1)(x2 Px2)....(xn Pxn)).
;            Donde xn es el número y Pxn es la probabilidad del número.
; Retorna: Una función λ que dado un valor [0,1[ devuelva un valor xi que siga la distribución según la tabla.
(define tabla
  (lambda (l)
    (lambda (k)
      (buscar-en-tabla k (acumulada l)))))

; Recibe: n = La cantidad de experimentos.
;         p = La probabilidad de éxito.
; Retorna: Una función λ que dado un valor k retorne la probabilidad de ser escogido.
; Ejemplo: (binomial 15 0.3).
(define binomial
  (lambda (n p)
    (lambda (k)
      (* (combinacion n k) (expt p k) (expt (- 1 p) (- n k))))))

; Recibe: p = La probabilidad de éxito.
; Retorna: Una función λ que dado un valor k retorne la probabilidad de ser escogido.
; Ejemplo: (geometrica 0.3).
(define geometrica
  (lambda (p)
    (lambda (k)
      (* (expt (- 1 p) k) p))))

; Recibe: N = Tamaño de la población.
;         d = Cantidad de los “exitosos”.
;         n = Muestra seleccionada.
; Retorna: Una función λ que dado un valor k retorne la probabilidad de ser escogido.
; Ejemplo: (hipergeometrica 100 15 30).
(define hipergeometrica
  (lambda (N d n)
    (lambda (k)
      (/ (* (combinacion d k) (combinacion (- N d) (- n k))) (combinacion N n)))))

; Recibe: m = Valor de la media.
; Retorna: Una función λ que dado un valor k retorne la probabilidad de ser escogido.
; Ejemplo: (poisson 3).
(define poisson
  (lambda (m)
    (lambda (k)
      (/ (* (expt m k) (exp (* m -1))) (factorial k)))))

; Recibe: l = Una lista de valores.
; Retorna: Una función λ que retorne de manera uniforme alguno de los valores de la lista.
; Ejemplo: (uniforme-disc '(2 4 6 8)).
(define uniforme-disc
  (lambda (l)
    (lambda ()
      (list-ref l (random (length l))))))

; Recibe: ini = Inicio del rango.
;         fin = Final del rango.
; Retorna: Una función λ que retorna de manera uniforma algún valor continuo entre el rango.
; Ejemplo: (uniforme 0 1).
(define uniforme
  (lambda (ini fin)
    (lambda ()
      (+ ini (random (- fin ini)) (random)))))

; Recibe: l = Valor para su lambda (1/μ).
; Retorna: Una función λ que dado un valor k retorne la probabilidad de ser escogido.
; Ejemplo: (exponencial 1/20).
(define exponencial
  (lambda (l)
    (lambda (k)
      (cond
        ((>= k 0) (* l (exp (* (* -1 l) k))))
        (else 0)))))

; Recibe: m = Media.
;         s = Desviación estándar.
; Retorna: Una función λ que dado un valor k retorne la probabilidad de ser escogido.
; Ejemplo (normal 0 1)
(define normal
  (lambda (m s)
    (lambda (k)
      (* 
       (/ 1 (* s (sqrt (* 2 PI))))
       (exp (* -1/2 (cuad (/ (- k m) s))))))))

; Función que se encarga de eviar los datos al servidor de las funciones de tipo discreta.
; Recibe: i = Contador de datos enviados.
;         n = Cantidad de datos a enviar.
;         table = Tabla acumulada de la distribución.
;         out = Puerto de escritura.
(define send-data-discrete
  (lambda (i n table out)
    (cond 
      ((< i n)
       (displayln (buscar-en-tabla (random) table) out)
       (send-data-discrete (+ i 1) n table out)))))

; Función que se encarga de eviar los datos al servidor de las funciones de tipo continuas.
; Recibe: i = Contador de datos enviados.
;         n = Cantidad de datos a enviar.
;         table = Tabla acumulada de la distribución.
;         out = Puerto de escritura.
(define send-data-continue
  (lambda (i n table out)
    (cond
      ((< i n)
       (displayln (buscar-en-tabla (random) table) out)
       (send-data-continue (+ i 1) n table out)))))

; Función que se encarga de eviar los datos al servidor de las funciones de tipo uniformes.
; Recibe: i = Contador de datos enviados.
;         n = Cantidad de datos a enviar.
;         func = Función a aplicar a los datos.
;         out = Puerto de escritura.
(define send-data-uniform
  (lambda (i n func out)
    (cond 
      ((< i n) 
       (displayln (func) out)
       (send-data-uniform (+ i 1) n func out)))))

(define send-name
  (lambda (args out)
    (displayln (first (last args)) out)))

(define send-range
  (lambda (ini fin interval out)
    (display ini out)
    (display ":" out)
    (display fin out)
    (display ":" out)
    (displayln interval out)))

; Función que se encarga de controlar las funciones discretas.
; Recibe: args = Argumentos del programa.
;         out = Puerto de escritura.
(define handle-discrete
  (lambda (args out)
    (send-name args out)
    (send-range (first (third args)) (second (third args)) 1 out)
    (send-data-discrete 0 (first args) (acumulada (generar-tabla-discreta (third args) (eval (last args)))) out)))

; Función que se encarga de controlar las funciones continuas.
; Recibe: args = Argumentos del programa.
;         out = Puerto de escritura.
(define handle-continue
  (lambda (args out)
    (send-name args out)
    (send-range (first (third args)) (second (third args)) (third (third args)) out)
    (send-data-continue 0 (first args) (acumulada (generar-tabla-continua (third args) (lambda (ini fin) (simpson (eval (last args)) ini fin)))) out)))

; Función que se encarga de controlar las funciones uniformes.
; Recibe: args = Argumentos del programa.
;         out = Puerto de escritura.
(define handle-uniform
  (lambda (args out)
    (send-name args out)
    (send-range (first (third args)) (second (third args)) (third (third args)) out)
    (send-data-uniform 0 (first args) (eval (last args)) out)))

; Función que define que tipo de función se quiere ejecutar.
; Recibe: args = Argumentos del programa.
;         out = Puerto de escritura.
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

;(start "binomial.txt" "localhost" 2020)
;(start "geometrica.txt" "localhost" 2020)
;(start "hipergeometrica.txt" "localhost" 2020)
;(start "poisson.txt" "localhost" 2020)
;(start "uniforme-disc.txt" "localhost" 2020)
;(start "uniforme.txt" "localhost" 2020)
;(start "exponencial.txt" "localhost" 2020)
;(start "normal.txt" "localhost" 2020)
;(start "lambda.txt" "localhost" 2020)