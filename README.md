# Ruído Ocupacional — AV1

- **Grupo:** Arthur Silva Soares.
- **Integrante:** ARTHUR SILVA SOARES — RA 2571392322010.
- **Responsável:** Ambiente, Sensor, autenticação e formulários.
- **Repositório:** https://github.com/Arthursooares/ruido-ocupacional.

Cadastro de ambientes de trabalho e sensores para um futuro sistema de monitoramento de ruído ocupacional, relacionado ao ODS 3. Permite organizar os registros e identificar onde cada sensor está instalado.

## Tecnologias e estrutura

Base: [aulas do professor](https://github.com/antoniorcn/fatec-2026-2s), pasta `itq-dsm-ldm`, revisão `d817afa930afb90427d220c8c16f930fc8100bd5`.

- **Backend, aula 08:** Kotlin 2.3.21, Spring Boot 4.1.1, Java 17, Gradle 9.7.1, MariaDB, JPA, Validation, Spring Security e JJWT 0.13.0.
- **Frontend, aula 10:** Kotlin 2.4.20, Compose Multiplatform 1.12.1, Material 3 1.12.0-alpha03, Gradle 9.5.1 e AGP 9.1.1.
- `backend/src/main/kotlin/edu/curso/projetos`: `model`, `repository`, `service`, `controller`, `dto`, `mapper` e `security`.
- `backend/http/av1.http`: requisições para demonstrar autenticação e os dois CRUDs.
- `frontend/shared`: modelos, formulários e listas compartilhados; `androidApp`, `desktopApp` e `webApp`: entradas de cada plataforma.
- Fluxo: Controller → Service → Repository → MariaDB. DTOs e mappers separam os dados HTTP das entidades.

Ambiente: nome, setor, descrição, ativo e data de cadastro. Sensor: nome, código único, modelo, ativo e data de instalação, com vínculo obrigatório a Ambiente. IDs são Long gerados pelo banco. Não é permitido excluir ambiente com sensores.

## Instalação e backend no Ubuntu

Extraia os dois ZIPs na mesma pasta, mantendo `backend/`, `frontend/`, `scripts/` e `.env.example`.

```bash
sudo apt update
sudo apt install openjdk-17-jdk openjdk-21-jdk mariadb-server openssl
sudo systemctl enable --now mariadb
sudo mariadb
```

No MariaDB, substitua o marcador por uma senha local:

```sql
CREATE DATABASE ruido_ocupacional CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'ruido_app'@'localhost' IDENTIFIED BY 'SUBSTITUA_LOCALMENTE';
GRANT ALL PRIVILEGES ON ruido_ocupacional.* TO 'ruido_app'@'localhost';
```

Na raiz, copie `.env.example` para `.env`, execute `chmod 600 .env` e preencha `DB_PASSWORD`. Gere o `JWT_SECRET` com `openssl rand -hex 32` e coloque o resultado em `.env`. Execute:

```bash
source .env
cd backend
./gradlew bootRun
```

API: `http://localhost:8080`. Apenas `POST /signup` e `POST /signin` são públicos. Use o JWT retornado no cabeçalho `Authorization: Bearer TOKEN`; validade de uma hora. As rotas `/ambientes` e `/sensores` possuem POST, GET, GET/PUT/DELETE por `/{id}` e GET `/buscar?nome=trecho`. Datas: `AAAA-MM-DD`. Demonstre usando `backend/http/av1.http`.

O perfil `dev` usa `ddl-auto=update`; `prod` usa `validate` e requer esquema previamente provisionado. Credenciais e segredo JWT ficam em variáveis de ambiente. Produção não faz parte do aceite acadêmico.

## Execução no Windows — PowerShell

Instale os [JDKs 17 e 21](https://learn.microsoft.com/en-us/java/openjdk/install), o [MariaDB pelo instalador MSI](https://mariadb.com/docs/server/server-management/install-and-upgrade-mariadb/installing-mariadb/binary-packages/installing-mariadb-msi-packages-on-windows) e, para Android, o [Android Studio](https://developer.android.com/studio). No instalador do MariaDB, configure o serviço, habilite TCP/IP na porta 3306 e escolha a senha de root. Abra o cliente MariaDB no menu Iniciar e execute o mesmo SQL da seção Ubuntu para criar banco e usuário.

Extraia os ZIPs na mesma pasta. No terminal PowerShell do VS Code, ajuste os caminhos abaixo para suas instalações. O JDK 21 executa o Gradle; o backend também precisa encontrar o JDK 17 instalado.

```powershell
Set-Location 'C:\Projetos\ruido-ocupacional'
$env:JAVA_HOME = 'C:\CAMINHO\DO\JDK-21'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
java -version

$env:DB_URL = 'jdbc:mariadb://localhost:3306/ruido_ocupacional'
$env:DB_USERNAME = 'ruido_app'
$senha = Read-Host 'Senha do usuario ruido_app' -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new('', $senha).Password
$env:SPRING_PROFILES_ACTIVE = 'dev'
$bytes = New-Object byte[] 32
$gerador = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$gerador.GetBytes($bytes)
$gerador.Dispose()
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)

Set-Location backend
.\gradlew.bat bootRun
```

As variáveis valem para esse terminal; repita a configuração ao abrir outro. O `.env` não é carregado automaticamente pelo Spring, e `source .env` é um comando Bash, não PowerShell. Aguarde `Started ProjetosApplication`; `bootRun` permanece ativo até `Ctrl+C`. A API e as requisições de `http/av1.http` são as mesmas do Ubuntu. Não é necessário instalar Gradle globalmente.

Para o frontend, abra outro terminal na raiz do projeto, configure o JDK 21 e entre em `frontend`. Execute um alvo por vez:

```powershell
Set-Location frontend
.\gradlew.bat :desktopApp:run
# Encerre o Desktop antes de executar o Web:
.\gradlew.bat :webApp:jsBrowserDevelopmentRun
```

Para Android, configure o mesmo SDK descrito abaixo pelo Android Studio. No PowerShell, o caminho usual é `$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"`; ajuste se instalou em outro local. Dentro de `frontend`:

```powershell
.\gradlew.bat :androidApp:assembleDebug
& "$env:ANDROID_HOME\platform-tools\adb.exe" devices
& "$env:ANDROID_HOME\platform-tools\adb.exe" install -r .\androidApp\build\outputs\apk\debug\androidApp-debug.apk
```

Os scripts `.sh` são auxiliares para Linux. Os comandos Windows foram documentados com base nas ferramentas oficiais; a execução foi validada no Ubuntu, não no Windows.

## Frontend

Use JDK 21. Dentro de `frontend`:

```bash
./gradlew :desktopApp:run
./gradlew :webApp:jsBrowserDevelopmentRun
```

Abra o endereço informado pelo servidor Web. Se a porta conflitar com a API, execute as demonstrações separadamente.

Para Android, configure SDK plataforma 37.0, Build Tools 36.0.0 e Platform Tools no Android Studio, aceite as licenças e defina `ANDROID_HOME` ou `frontend/local.properties`. Alternativa: `scripts/configurar-android.sh`, a partir da raiz.

```bash
cd frontend
./gradlew :androidApp:assembleDebug
adb install -r androidApp/build/outputs/apk/debug/androidApp-debug.apk
```

Use emulador ou aparelho com API 24 ou superior. Grave um ambiente, selecione-o ao gravar um sensor e teste **Limpar Campos** nos dois formulários. Os dados ficam em memória e são perdidos ao encerrar ou recarregar a aplicação. O protótipo possui inclusão e listagem, sem edição/exclusão nem comunicação com a API; autenticação e persistência são demonstradas no backend.
