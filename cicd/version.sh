MY_PATH="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/$(basename "${BASH_SOURCE[0]}")"
MY_DIR="$(dirname $MY_PATH)"
echo ${MY_DIR}
source ${MY_DIR}/build-version.sh
export BERLIOZ_SDK_VERSION="1.1.${BERLIOZ_SDK_BUILD_VERSION}-SNAPSHOT"
echo ${BERLIOZ_SDK_VERSION}