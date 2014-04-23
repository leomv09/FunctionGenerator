(define pi 3.14159265359)

(define read-file
  (lambda (path)
    (let ((port (open-input-file path)))
      (let funct ((next-object (read port)))
        (cond 
          ((eof-object? next-object) (begin (close-input-port port) '()))
          (else (cons next-object (funct (read port)))))))))

;Función que calcula el factorial de un número dado.
;Recibe: n = Número al que se quiere saber el factorial.
;Retorna: El factorial del número ingresado.
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

(define acumulada-aux
  (lambda (l1 l2)
    (cond
      ((null? l1) l2)
      ((null? l2) (acumulada-aux (cdr l1) (append l2 (list (car l1)))))
      (else (acumulada-aux (cdr l1) (append l2 (list (list (caar l1) (+ (cadar l1) (second (last l2)))))))))))

(define acumulada
  (lambda (l)
    (acumulada-aux l '())))

(define buscar-en-tabla
  (lambda (x l)
    (cond
      ((<= x (cadar l)) (caar l))
      (else (buscar-en-tabla x (cdr l))))))

; Recibe: ini = Inicio del rango.
;         fin = Final del rango.
;         fun = Una función para aplicarle a cada numero en [ini, fin].
; Retorna: Una lista que contiene un par de tipo (x (fun x)) donde x es cada elemento del rango.
; Ejemplo: (generar-tabla-con-rango '(0 3) (lambda (n) (* n 2))) => '((0 0) (1 2) (2 4) (3 6)).
(define generar-tabla-con-rango
  (lambda (rango fun)
    (cond
      ((> (first rango) (last rango)) '())
      (else (append (list (list (first rango) (fun (first rango)))) (generar-tabla-con-rango (list (+ (first rango) 1) (last rango)) fun))))))

; Recibe: l = Una lista de valores.
;         fun = Una función para aplicarle a cada elemento de la lista.
; Retorna: Una lista que contiene un par de tipo (x (fun x)) donde x es cada elemento de la lista.
; Ejemplo: (generar-tabla-con-lista '(2 4 6 8) (lambda (n) (* n 2))) => '((2 4) (4 8) (6 12) (8 16)).
(define generar-tabla-con-lista
  (lambda (l fun)
    (map (lambda (x) (list x (fun x))) l)))

; *** Tabla ***

;Recibe: l = Una lista de listas con el siguiente formato: '((x1 Px1)(x2 Px2)....(xn Pxn)). Donde xn es el número y Pxn es la probabilidad del número.
;Retorna: Una función λ que dado un valor [0,1[ devuelva un valor xi que siga la distribución según la tabla.
(define tabla
  (lambda (l)
    (lambda (k)
      (buscar-en-tabla k (acumulada l)))))

; *** Distribución Binomial ***

;Recibe: n = El tamaño de la cantidad de experimentos.
;        p = Probabilidad de éxito.
;Retorna: Una función λ que dado un valor [0,1[ devuelva un valor x que siga una distribución binomial.
;Ejemplo: (binomial 15 0.3)
(define binomial
  (lambda (n p)
    (lambda (k)
      (* (combinacion n k) (expt p k) (expt (- 1 p) (- n k))))))


; *** Distribución Hipergeaométrica ***

; Recibe: N = Tamaño de la población.
;         d = Cantidad de los “exitosos”.
;         n = Muestra seleccionada.
; Retorna: Una función λ que dado un valor [max{0, n-(N-d)}, min{n, b}] devuelva la probabilidad de ser escogido.
; Ejemplo: (hipergeometrica 100 15 30) => #<procedure:lambda>.
(define hipergeometrica
  (lambda (N d n)
    (lambda (k)
      (/ (* (combinacion d k) (combinacion (- N d) (- n k))) (combinacion N n)))))

; *** Distribución Uniforme Discreta ***

; Recibe: l = Una lista de valores todos con la misma probabilidad de ser escogidos.
; Retorna: Una función λ que retorne de manera uniforme alguno de los valores de la lista.
; Ejemplo: (uniforme-disc '(2 4 6 8)) => #<procedure:lambda>.
(define uniforme-disc
  (lambda (l)
    (lambda ()
      (list-ref l (random (length l))))))

; *** Uniforme Continua ***

(define uniforme
  (lambda (ini fin)
    (lambda ()
      (+ ini (random (- fin ini)) (random)))))

; *** Distrubución Geométrica ***

;Recibe: p = Una probabilidad de exito p con 0 <= p <= 1.
;        n = Rango para la función acumulada.
;Retorna: Una función λ que dado un valor k retorne la probabilidad de distribución.
;Ejemplo: (geometrica 0.3) => #<procedure:lambda>.
(define geometrica
  (lambda (n p)
    (lambda (k)
      (* (expt (- 1 p) k) p))))


; *** Distribución de Poisson ***

;Recibe: m = Valor de la media (λ).
;Retorna: Una función λ que dado un valor k retorne la probabilidad de distribución.
;Ejemplo: (poisson 3) => #<procedure:lambda>.
(define poisson
  (lambda (m)
    (lambda (k)
      (/ (* (expt m k) (exp (* m -1))) (factorial k)))))

(define exponencial
  (lambda (u)
    (lambda (k)
      (cond
        ((>= k 0) (* (/ 1 u) (exp (* (- (/ 1 u) k)))))
        (else 0)))))

(define normal
  (lambda (m s)
    (lambda (k)
      (* 
       (/ 1 (* s (sqrt (* 2 pi))))
       (exp (* -1/2 (cuad (/ (- x m) s))))))))

(define normal-estandar (normal 0 1))

(define data-sender-disc
  (lambda (i n table out)
    (cond ((< i n) 
           (display (buscar-en-tabla (random) table) out)
           (display "\n" out)
           (data-sender-disc (+ i 1) n table out)))))

(define data-sender-uniform
  (lambda (i n func out)
    (cond ((< i n) 
           (display (func) out)
           (display "\n" out)
           (data-sender-uniform (+ i 1) n func out)))))

(define disc-handler
  (lambda (args out)
    (display "0\n" out)
    (display (first (list-ref args 2)) out)
    (display "\n" out)
    (display (last (list-ref args 2)) out)
    (display "\n" out)
    (data-sender-disc 0 (first args) (acumulada (generar-tabla-con-rango (list-ref args 2) (eval (last args)))) out)))

(define cont-handler
  (lambda (args out)
    (random)))

(define uniform-handler
  (lambda (args out)
    (display "2\n0\n0\n" out)
    (data-sender-uniform 0 (first args) (eval (last args)) out)))

(define function-controler
  (lambda (args)
    (let-values ([(in out) (tcp-connect "localhost" 2020)])
      (case(list-ref args 1)
        ('discreta (disc-handler args out))
        ('continua (cont-handler args out))
        ('uniforme (uniform-handler args out)))
      (close-output-port out))))

(define control-handler 
  (lambda (path)
    (function-controler (read-file path))))