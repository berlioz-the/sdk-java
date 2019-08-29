MY_PATH="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/$(basename "${BASH_SOURCE[0]}")"
MY_DIR="$(dirname $MY_PATH)"
source ${MY_DIR}/build-version.sh
echo "export BERLIOZ_SDK_BUILD_VERSION=$((BERLIOZ_SDK_BUILD_VERSION+1))" > ${MY_DIR}/build-version.sh
source ${MY_DIR}/version.sh
