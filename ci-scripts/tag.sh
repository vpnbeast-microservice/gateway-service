#!/bin/bash

function mvn_get_version() {
    # shellcheck disable=SC2046
    # shellcheck disable=SC2005
    echo "$(mvn -q \
        -Dexec.executable=echo \
        -Dexec.args='${project.version}' \
        --non-recursive \
        exec:exec | cut -d "=" -f 2)"
}

function mvn_set_version() {
    mvn versions:set -DnewVersion="${1}"
    mvn versions:commit -B
}

function mvn_increment_minor_version() {
    local version_major, version_minor, version_patch
    version_major=$(echo "$1" | cut -d "." -f 1)
    version_minor=$(echo "$1" | cut -d "." -f 2)
    version_patch=$(echo "$1" | cut -d "." -f 3)
    # shellcheck disable=SC2003
    version_patch=$(expr "${version_patch}" + 1)
    echo "${version_major}.${version_minor}.${version_patch}"
}

function set_chart_version() {
    sed -i "s/${1}/${2}/g" charts/"${CHART_NAME}"/Chart.yaml
    sed -i "s/${1}/${2}/g" charts/"${CHART_NAME}"/values.yaml
}

set -ex
CHART_NAME=gateway-service
CURRENT_VERSION=$(mvn_get_version)
RELEASE_VERSION=$(mvn_increment_minor_version "${CURRENT_VERSION}")

mvn_set_version "$RELEASE_VERSION"
set_chart_version "${CURRENT_VERSION}" "${RELEASE_VERSION}"