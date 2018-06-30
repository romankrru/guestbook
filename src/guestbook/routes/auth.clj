(ns guestbook.routes.auth
  (:require [compojure.core :refer [defroutes GET POST]]
            [guestbook.views.layout :as layout]
            [noir.response :refer [redirect]]
            [hiccup.form :refer
             [form-to label text-field password-field submit-button]]
            [noir.session :as session]))

(defn control [field name text]
  (list (label name text)
        (field name)
        [:br]))

(defn registration-page []
  (layout/common
    (form-to [:post "/register"]
             (control text-field :id "screen name")
             (control password-field :pass "password")
             (control password-field :pass1 "retype-password")
             (submit-button "create account"))))

(defn login-page []
  (layout/common
    (form-to [:post "/login"]
             (control text-field :id "screen name")
             (control password-field :pass "Password")
             (submit-button "create account"))))

(defroutes auth-routes
  (GET "/register" [] (registration-page))
  (POST "/register" [id pass pass1]
    (if (= pass pass1)
      (redirect "/")
      (registration-page)))
  (GET "/login" [] (login-page))
  (POST "/login" [id pass]
    (session/put! :user id)
    (redirect "/")))
