(ns fp-first.util.parse-file)

(import '(au.com.bytecode.opencsv.CSVReader))

(defn parse-fl [fl-nm]
  (def f-reader (new java.io.FileReader fl-nm))
  (def reader (new au.com.bytecode.opencsv.CSVReader f-reader))
  (def points  (transient []) )
  (def rcrd (.readNext reader))
    (while (not (nil? rcrd))
      (do
          (conj! points (vec (map read-string (pop (vec rcrd)))))
          (def rcrd (.readNext reader))
      )
    )
  points
)
