#!/bin/bash
set -e

echo "Starting Apache Ignite ${IGNITE_VERSION} with custom configuration..."

exec $IGNITE_HOME/bin/ignite.sh $IGNITE_HOME/config/ignite-config.xml
