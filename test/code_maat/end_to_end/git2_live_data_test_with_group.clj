;;; Copyright (C) 2015 Adam Tornhill
;;;
;;; Distributed under the GNU General Public License v3.0,
;;; see http://www.gnu.org/licenses/gpl.html

(ns code-maat.end-to-end.git2-live-data-test-with-group
  (:require [code-maat.app.app :as app])
  (:use clojure.test))

(def ^:const git-log-file "./test/code_maat/end_to_end/roslyn_git.log")
(def ^:const text-group-file "./test/code_maat/end_to_end/text-layers-definition.txt")

(deftest parses-live-data-with-text-group
  (is (= (with-out-str
           (app/run git-log-file
                    {:version-control "git2"
                     :analysis "revisions"
                     :group text-group-file
                     }))
         (clojure.string/join "\n"
          ["entity,n-revs"
           "Interactive Layer,3"
           "Editor Layer,3\n"
           ]))))
