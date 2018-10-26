#!/system/bin/sh
# Please don't hardcode /magisk/modname/... ; instead, please use $MODDIR/...
# This will make your scripts compatible even if Magisk change its mount point in the future
MODDIR=${0%/*}

# This script will be executed in post-fs-data mode
# More info in the main Magisk thread

RIRU_MODULES_DIR="/data/misc/riru/modules"
DATA_DIRECTORY="$RIRU_MODULES_DIR/ibr"

if [ ! -d "$RIRU_MODULES_DIR" ];then
    exit 1
fi

mkdir -p "$DATA_DIRECTORY"
mount -o bind "$MODDIR/data" "$DATA_DIRECTORY"
