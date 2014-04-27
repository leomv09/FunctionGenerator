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
  (lambda (p)
    (lambda (k)
      (* (expt (- 1 p) k) p))))

;Recibe: m = Valor de la media.
;Retorna: Una función λ que dado un valor k retorne la probabilidad de ser escogido.
;Ejemplo: (poisson 3).
(define poisson
  (lambda (m)
    (lambda (k)
      (/ (* (expt m k) (exp (* m -1))) (factorial k)))))

(define exponencial
  (lambda (u)
    (lambda (k)
      (cond
        ((>= k 0) (* u (exp (* (* -1 u) k))))
        (else 0)))))

(define normal
  (lambda (m s)
    (lambda (k)
      (* 
       (/ 1 (* s (sqrt (* 2 PI))))
       (exp (* -1/2 (cuad (/ (- k m) s))))))))

(define normal-estandar (normal 0 1))

(define send-data-discrete
  (lambda (i n table out)
    (cond 
      ((< i n)
       (displayln (buscar-en-tabla (random) table) out)
       (send-data-discrete (+ i 1) n table out)))))

(define send-data-continue
  (lambda (i n func out)
    (cond
      ((< i n)
       (displayln "Not implemented.")
       (send-data-continue n n func out)))))

(define send-data-uniform
  (lambda (i n func out)
    (cond 
      ((< i n) 
       (displayln (func) out)
       (send-data-uniform (+ i 1) n func out)))))

(define handle-discrete
  (lambda (args out)
    (send-data-discrete 0 (first args) (acumulada (generar-tabla-discreta (third args) (eval (last args)))) out)))

(define handle-continue
  (lambda (args out)
    (send-data-continue 0 (first args) (eval (last args)) out)))

(define handle-uniform
  (lambda (args out)
    (send-data-uniform 0 (first args) (eval (last args)) out)))

(define function-switch
  (lambda (args out)
    (case (second args)
      ('discreta (handle-discrete args out))
      ('continua (handle-continue args out))
      ('uniforme (handle-uniform args out)))))

(define start 
  (lambda (path host port)
    (with-handlers
        ([exn:fail:network? (lambda (e) (displayln "Failed To Connect To Socket."))]
         [exn:fail:filesystem? (lambda (e) (displayln "Failed To Open File."))])
      (let-values ([(in out) (tcp-connect host port)])
        (function-switch (read-file path) out)
        (close-output-port out)))))

(start "archivo1.txt" "localhost" 2020)