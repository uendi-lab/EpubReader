# 🔧 SOLUZIONE RAPIDA - Errore Gradle

## Problema
Stai usando **Gradle 9.0-milestone-1** (versione instabile) che causa errori di compilazione con `jlink.exe`.

## ✅ Soluzione in 3 Passaggi

### **Metodo 1: Tramite Android Studio (CONSIGLIATO)**

1. **Apri il progetto** in Android Studio
2. **Vai su**: `File → Project Structure` (Ctrl+Alt+Shift+S)
3. **Seleziona**: `Project` nel menu a sinistra
4. **Cambia** "Gradle Version" da `9.0-milestone-1` a **`8.2`**
5. Clicca **"OK"**
6. Android Studio sincronizzerà automaticamente

### **Metodo 2: Modifica Manuale File**

1. **Apri** il file: `gradle/wrapper/gradle-wrapper.properties`
2. **Trova** la riga: `distributionUrl=...`
3. **Sostituisci** con:
```
distributionUrl=https\://services.gradle.org/distributions/gradle-8.2-bin.zip
```
4. **Salva** il file
5. In Android Studio: `File → Sync Project with Gradle Files`

### **Metodo 3: Pulisci Cache Gradle**

Se i metodi sopra non funzionano:

1. **Chiudi** Android Studio
2. **Elimina** queste cartelle:
   - `C:\Users\prift\.gradle\caches`
   - Nella cartella del progetto: `.gradle` (cartella nascosta)
3. **Riapri** Android Studio
4. Lascia che sincronizzi nuovamente

## 🚀 Dopo la Correzione

1. **Sync Gradle**: `File → Sync Project with Gradle Files`
2. **Clean Build**: `Build → Clean Project`
3. **Rebuild**: `Build → Rebuild Project`
4. **Run** l'app ▶️

## ⚠️ Se Persiste l'Errore

Prova questo in sequenza:

```bash
# Nel terminale di Android Studio
gradlew clean
gradlew build --refresh-dependencies
```

Oppure:

1. `File → Invalidate Caches → Invalidate and Restart`
2. Aspetta il riavvio
3. Riprova la build

## 📝 Nota Tecnica

L'errore si verifica perché:
- Gradle 9.0 è una **milestone** (pre-release)
- Ha incompatibilità con il JDK bundled di Android Studio
- Gradle 8.2 è la versione **stabile** consigliata

## ✅ Versioni Consigliate

- **Gradle**: 8.2 o 8.3
- **Android Gradle Plugin**: 8.2.0 (già impostato)
- **JDK**: 17 (già bundled in Android Studio)

---

**Il progetto compilerà senza problemi con Gradle 8.2!** 🎉
