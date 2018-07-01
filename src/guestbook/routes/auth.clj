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

(defn login-page [& [error]]
  (layout/common
    (if error
      [:div.error "Login Error: " error]
      (form-to [:post "/login"]
             (control text-field :id "screen name")
             (control password-field :pass "Password")
             (submit-button "create account")))))

(defn handle-login [id pass]
  (cond
    (empty? id)
    (login-page "screen name is required")
    (empty? pass)
    (login-page "password is required")
    (and (= "foo" id) (= "bar" pass))
    (do
      (session/put! :user id)
      (redirect "/"))
    :else
    (login-page "authentication failed")))

(defroutes auth-routes
  (GET "/register" [] (registration-page))
  (POST "/register" [id pass pass1]
    (if (= pass pass1)
      (redirect "/")
      (registration-page)))
  (GET "/login" [] (login-page))
  (POST "/login" [id pass] (handle-login id pass))
  (GET "/logout" []
    (layout/common
      (form-to [:post "/logout"]
               (submit-button "logout"))))
  (POST "/logout" []
    (session/clear!)
    (redirect "/")))
