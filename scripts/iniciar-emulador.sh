#!/usr/bin/env bash
set -euo pipefail
projeto_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"
export ANDROID_HOME="$projeto_dir/.tools/android-sdk"
export ANDROID_AVD_HOME="$projeto_dir/.tools/avd"
if [[ ! -x "$ANDROID_HOME/emulator/emulator" || ! -d "$ANDROID_HOME/system-images/android-30/default/x86_64" ]]; then
    printf '%s\n' 'Instale o SDK com scripts/configurar-android.sh e então execute:' \
        "\"$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager\" --sdk_root=\"$ANDROID_HOME\" emulator 'system-images;android-30;default;x86_64'"
    exit 1
fi
mkdir -p "$ANDROID_AVD_HOME"
if [[ ! -f "$ANDROID_AVD_HOME/RuidoAV1.ini" ]]; then
    printf 'no\n' | "$ANDROID_HOME/cmdline-tools/latest/bin/avdmanager" create avd \
        -n RuidoAV1 -k 'system-images;android-30;default;x86_64' -d pixel_2
    python3 - "$ANDROID_AVD_HOME/RuidoAV1.avd/config.ini" <<'PY'
from pathlib import Path
import sys
p = Path(sys.argv[1])
lines = p.read_text().splitlines()
updates = {'avd.id':'RuidoAV1', 'avd.name':'RuidoAV1', 'hw.lcd.width':'720',
    'hw.lcd.height':'1280', 'hw.lcd.density':'320', 'hw.camera.back':'none',
    'hw.camera.front':'none', 'disk.dataPartition.size':'2G'}
for key, value in updates.items():
    lines = [line for line in lines if not line.startswith(key + '=')]
    lines.append(key + '=' + value)
p.write_text('\n'.join(lines) + '\n')
PY
fi
exec "$ANDROID_HOME/emulator/emulator" -avd RuidoAV1 -memory 2048 -cores 2 \
    -gpu swiftshader -no-audio -no-boot-anim -no-snapshot
