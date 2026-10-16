#!/usr/bin/env bash
# Instala SDK local ao projeto. As licenças são apresentadas interativamente.
set -euo pipefail
projeto_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"
sdk_dir="$projeto_dir/.tools/android-sdk"
archive="$projeto_dir/.tools/downloads/commandlinetools-linux-15859902_latest.zip"
checksum=4e4c464f145a7512b57d088ac6c278c03c9eea610886b35a5e0804e74eedf583
for comando in java curl sha256sum python3; do
    command -v "$comando" >/dev/null || { echo "Instale o pré-requisito: $comando" >&2; exit 1; }
done
if [[ ! -x "$sdk_dir/cmdline-tools/latest/bin/sdkmanager" ]]; then
    printf '%s\n' 'O Android SDK exige aceite da licença do Google:' \
        'https://developer.android.com/studio#terms-and-conditions'
    read -r -p 'Você leu e aceita a licença do Android SDK? Digite ACEITO: ' aceite
    [[ "$aceite" == ACEITO ]] || { echo 'Instalação cancelada, sem aceite de licença.'; exit 1; }
    mkdir -p "$(dirname -- "$archive")" "$sdk_dir/cmdline-tools"
    if [[ ! -f "$archive" ]]; then
        curl --fail --location --retry 2 \
            https://dl.google.com/android/repository/commandlinetools-linux-15859902_latest.zip \
            --output "$archive"
    fi
    printf '%s  %s\n' "$checksum" "$archive" | sha256sum --check
    python3 - "$archive" "$sdk_dir/cmdline-tools" <<'PY'
from pathlib import Path
from zipfile import ZipFile
import sys
archive, parent = Path(sys.argv[1]), Path(sys.argv[2])
with ZipFile(archive) as z:
    z.extractall(parent)
source = parent / 'cmdline-tools'
source.rename(parent / 'latest')
for script in (parent / 'latest/bin').iterdir():
    if script.is_file(): script.chmod(0o755)
PY
fi
# Nenhum yes automático: os termos são apresentados pelo gerenciador oficial.
"$sdk_dir/cmdline-tools/latest/bin/sdkmanager" --sdk_root="$sdk_dir" \
    'platform-tools' 'platforms;android-37.0' 'build-tools;36.0.0'
# Gerado somente após sucesso da instalação. Nunca versionar o caminho da máquina.
printf 'sdk.dir=%s\n' "$sdk_dir" > "$projeto_dir/frontend/local.properties"
printf '\nSDK configurado em %s\n' "$sdk_dir"
printf '%s\n' "Compile com: cd \"$projeto_dir/frontend\" && ./gradlew :androidApp:assembleDebug"
