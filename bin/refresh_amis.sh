#!/usr/bin/env bash

regions=(us_west_2 us_west_1 us_east_1 eu_west_1 eu_central_1)

trusty=trusty-14.04
xenial=xenial-16.04
tmp=$(mktemp)

cat <<EOF
---
amis:
EOF

function aws_describe_images {
    region=$1
    release=$2
    latest=$3
    aws --region $region ec2 describe-images \
        --filters \
        "Name=name,Values=ubuntu/images/hvm-ssd/ubuntu-$release-amd64-server-$latest" \
        "Name=is-public,Values=true" "Name=owner-id,Values=099720109477"
}

# latest Xenial
aws_describe_images us-west-2 $xenial '*' |\
    egrep "\<Name\>"|awk -F'-' '{print $NF}'|sed -e 's/[","]//g'|sort -rn|head -n 1 \
    > $tmp
xlatest=`cat $tmp`

# latest Trusty
aws_describe_images us-west-2 $trusty '*' |\
    egrep "\<Name\>"|awk -F'-' '{print $NF}'|sed -e 's/[","]//g'|sort -rn|head -n 1 \
    > $tmp
tlatest=`cat $tmp`

# Xenial
echo "  xenial:"
for region in ${regions[*]}; do
    r=`echo $region|sed -e 's/_/-/g'`
    echo -n "    $region: "
    aws_describe_images $r $xenial $xlatest |\
        grep ImageId | awk -F: '{print $2}'|sed -e 's/[",]//g'|head -n 1
done

# Trusty
echo "  trusty:"
for region in ${regions[*]}; do
    r=`echo $region|sed -e 's/_/-/g'`
    echo -n "    $region: "
    aws_describe_images $r $trusty $tlatest |\
        grep ImageId | awk -F: '{print $2}'|sed -e 's/[",]//g'|head -n 1
done
