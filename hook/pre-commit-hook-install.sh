#!/bin/sh
cd "$(dirname "$0")"
touch .git/hooks/pre-commit
rm .git/hooks/pre-commit
ln -s ../../pre-commit-hook.sh .git/hooks/pre-commit