package main

import (
	"fmt"
	"os"
	"os/exec"
)

func echoPath() {
	var currentPath, err = os.Getwd()
	fmt.Printf("current path is %s\n", currentPath)
	if err != nil {
		fmt.Printf("catch error %s", err)
	}
}

func echoExePath() {
	path, err := exec.LookPath(os.Args[0])
	fmt.Printf("exe path is %s", path)
	if err != nil {
		fmt.Printf("catch error %s", err)
	}
}
