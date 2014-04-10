(define factorial
  (lambda (n)
    (cond
      ((= n 0) 1)
      (else (* n (factorial (- n 1)))))))

(define combinacion
  (lambda (n k)
    (/ (factorial n) (* (factorial k) (factorial (- n k))))))

(define acumulada-aux
  (lambda (l1 l2)
    (cond
      ((null? l1) l2)
      ((= (length l1) 1) (acumulada-aux (cdr l1) (append l2 (list ( list (caar l1) 1)))))
      ((null? l2) (acumulada-aux (cdr l1) (append l2 (list (car l1)))))
      (else (acumulada-aux (cdr l1) (append l2 (list (list (caar l1) (+ (cadar l1) (cadar (reverse l2)))))))))))

(define acumulada
  (lambda (l)
    (acumulada-aux l '())))

(define buscar-en-tabla
  (lambda (x l)
    (cond
      ((<= x (cadar l)) (caar l))
      (else (buscar-en-tabla x (cdr l))))))

(define generar-tabla
  (lambda (n fun)
    (cond
      ((< n 0) '())
      (else (append (generar-tabla (- n 1) fun) (list (list n (fun n))))))))

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
      (buscar-en-tabla k (acumulada (generar-tabla n (binomial-aux n p)))))))