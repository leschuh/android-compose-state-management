// Top-Level-Builddatei: Plugins nur deklarieren, angewendet werden sie im Modul :app.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
