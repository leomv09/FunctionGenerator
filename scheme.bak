(define read-file
  (lambda (path)
    (let ((port (open-input-file path)))
      (let funct ((next-object (read port)))
        (cond 
          ((eof-object? next-object) (begin (close-input-port port) '()))
          (else (cons next-object (funct (read port)))))))))

; Recibe: f = Una función para aplicarle a cada elemento entre [k, n[
;         k = Inicio de la suma (Incluyente).
;         n = Final de la suma (Excluyente).
; Retorna: La suma de la función f evaluada en cada elemento de [k, n[
; Ejemplo: (suma abs 0 5) => 10.
(define suma
  (lambda (f k n)
    (cond
      ((>= k n) 0)
      (else (+ (f k) (suma f (+ k 1) n))))))

; Recibe: f = Función a integrar.
;         a = Límite inferior de la integral.
;         b = Límite superior de la integral.
;         steps = Número de diviciones a realizar, entre más diviciones más precisión.
; Retorna: La integral desde a hasta b de f(x)dx.
; Ejemplo: (integral sin 0 1 1000) => 0.4596976941318605.
(define integral
  (lambda (f a b steps)
    (define h (/ (- b a) steps))
    (* (/ h 6) (suma (lambda(x) (+ (f (+ a (* h x))) (f (+ a (* h x) h)) (* 4 (f (+ a (* h x) (/ h 2)))))) 0 steps))))

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
; Ejemplo: (generar-tabla-con-rango 0 3 (lambda (n) (* n 2))) => '((0 0) (1 2) (2 4) (3 6)).
(define generar-tabla-con-rango
  (lambda (ini fin fun)
    (cond
      ((> ini fin) '())
      (else (append (list (list ini (fun ini))) (generar-tabla-con-rango (+ ini 1) fin fun))))))

; Recibe: l = Una lista de valores.
;         fun = Una función para aplicarle a cada elemento de la lista.
; Retorna: Una lista que contiene un par de tipo (x (fun x)) donde x es cada elemento de la lista.
; Ejemplo: (generar-tabla-con-lista '(2 4 6 8) (lambda (n) (* n 2))) => '((2 4) (4 8) (6 12) (8 16)).
(define generar-tabla-con-lista
  (lambda (l fun)
    (map (lambda (x) (list x (fun x))) l)))

(define tabla
  (lambda (l)
    (lambda (k)
      (buscar-en-tabla k (acumulada l)))))

(define binomial-aux
  (lambda (n p)
    (lambda (k)
      (* (combinacion n k) (expt p k) (expt (- 1 p) (- n k))))))

(define binomial
  (lambda (n p)
    (lambda (k)
      (buscar-en-tabla k (acumulada (generar-tabla-con-rango 0 n (binomial-aux n p)))))))

; Recibe: N = Tamaño de la población.
;         d = Cantidad de los “exitosos”.
;         n = Muestra seleccionada.
; Retorna: Una función λ que dado un valor [max{0, n-(N-d)}, min{n, b}] devuelva la probabilidad de ser escogido.
; Ejemplo: (hipergeometrica-aux 100 15 30) => #<procedure:lambda>.
(define hipergeometrica-aux
  (lambda (N d n)
    (lambda (k)
      (/ (* (combinacion d k) (combinacion (- N d) (- n k))) (combinacion N n)))))

; Recibe: N = Tamaño de la población.
;         d = Cantidad de los “exitosos”.
;         n = Muestra seleccionada.
; Retorna: Una función λ que dado un valor [0,1[ devuelva un valor x que siga una distribución hipergeométrica.
; Ejemplo: (hipergeometrica 100 15 30) => #<procedure:lambda>.
(define hipergeometrica
  (lambda (N d n)
    (lambda (k)
      (buscar-en-tabla k (acumulada (generar-tabla-con-rango (max 0 (- n (- N d))) (min d n) (hipergeometrica-aux N d n)))))))

; Recibe: l = Una lista de valores todos con la misma probabilidad de ser escogidos.
; Retorna: Una función λ que dado un valor k devuelva la probabilidad de ser escogido.
;          En este caso la probabilidad es igual para todos los valores.
; Ejemplo: (uniforme-disc-aux '(2 4 6 8)) => #<procedure:lambda>.
(define uniforme-disc-aux
  (lambda (l)
    (lambda (k)
      (/ 1 (length l)))))

; Recibe: l = Una lista de valores todos con la misma probabilidad de ser escogidos.
; Retorna: Una función λ que retorne de manera uniforme alguno de los valores de la lista.
; Ejemplo: (uniforme-disc '(2 4 6 8)) => #<procedure:lambda>.
(define uniforme-disc
  (lambda (l)
    (lambda ()
      (buscar-en-tabla (random) (acumulada (generar-tabla-con-lista l (uniforme-disc-aux l)))))))