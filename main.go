package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"strconv"
	"strings"
)

var picturesMap = map[string]map[*Picture]bool{}
var allPictures = []*Picture{}
var vertikale = []*Picture{}
var povprecjeTagov = 0.0

func main() {
	ss := NewSlideshow()
	readData()
	fixVerticals()
	sortInMap()

	var p *Picture
	tags := &map[string]bool{}
	for true {
		p = findNextPhoto(tags)
		if p == nil {
			break
		}
		ss.add(p)
		tags = p.Tags
	}

	// fmt.Println(povprecjeTagov)
	// i := 0
	// for _, v := range vertikale {
	// 	i += len(*v.Tags)
	// }
	// fmt.Println(i / (len(vertikale) / 2))

	fmt.Printf("%d\n%s", ss.NofSlides, ss.Out)
}

func readData() {
	sc := bufio.NewScanner(os.Stdin)

	sc.Scan()
	nString := sc.Text()
	n, err := strconv.Atoi(nString)
	if err != nil {
		panic(fmt.Errorf("x convert"))
	}

	j := 0.0
	for i := 0; i < n; i++ {
		sc.Scan()
		p := strings.Split(sc.Text(), " ") // prebrana vrstica

		tags := p[2:]                // tagi ki jih imamo
		tagsMap := map[string]bool{} // Set tagov - to se posleje v picutre struct, da lahko cekiramo z if _, ok = map[tag]; ok{ //obstaja }
		for _, t := range tags {
			tagsMap[t] = true
		}
		jeVertikala := p[0] == "V"
		pic := NewPicture(i, &tagsMap, jeVertikala) // Pointer na trenutn picture
		if jeVertikala {
			vertikale = append(vertikale, pic)
			povprecjeTagov = (povprecjeTagov*j + float64(len(tags))) / (j + 1.0)
			j++
		}

		allPictures = append(allPictures, pic) // dodaj sliko v seznam vseh slik
	}
}

// zdruzi tako da je cim vec tagov
func fixVerticals() {
	voidP := &Picture{}
	for _, p := range vertikale {
		if p.Merged {
			continue
		}
		best := 10000000 // wtf // verjteno ne bo nikoli vec kot 10000000 odstopa od pptagov
		bestP := voidP
		for _, v := range vertikale {
			if p == v || v.Merged {
				continue
			}

			i := p.NofTags + v.NofTags - intersectionOfTags(p.Tags, v.Tags) // st tagov v novi sliki
			if i < int(2*povprecjeTagov) {
				bestP = v
				break
			}
			razlika := int(math.Abs(povprecjeTagov - float64(i)))
			if razlika < best {
				best = razlika
				bestP = v
			}
		}
		if bestP == voidP {
			fmt.Print("fml")
		}
		p.Merge(bestP)
	}
}

func sortInMap() {
	for _, p := range allPictures {
		if p.Used {
			continue
		}
		for t := range *p.Tags {
			if picturesMap[t] == nil {
				picturesMap[t] = map[*Picture]bool{}
			}
			picturesMap[t][p] = true
		}
	}
}

// Polgeda koliko tagov se ujema
func intersectionOfTags(prejsnjaSlika, trenutnaSlika *map[string]bool) int {
	i := 0
	for a := range *trenutnaSlika {
		if _, ok := (*prejsnjaSlika)[a]; ok {
			i++
		}
	}
	return i
}

func findNextPhoto(tags *map[string]bool) *Picture {
	checked := map[*Picture]bool{}
	b := len(*tags)
	// bestP := &Picture{}
	// emptyP := bestP
	// bestPoints := 0

	for t := range *tags {
		for p := range picturesMap[t] {
			if p.Used {
				continue
			} else if _, ok := checked[p]; ok {
				continue
			} else {
				checked[p] = true
			}
			i := intersectionOfTags(tags, p.Tags)
			a := len(*p.Tags)
			if i != 0 && (a-1) != 0 && (b-i) != 0 {
				return p
			}
			// points := min(i, min(a-i, b-i))
			// if points > 5 {
			// 	return p
			// }
			// if points > bestPoints {
			// 	bestP = p
			// 	bestPoints = points
			// }
		}
	}

	// if bestP != emptyP {
	// 	return bestP
	// }
	for _, p := range allPictures {
		if !p.Used {
			return p
		}
	}

	return nil
}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}

type Picture struct {
	ID1      int
	ID2      int
	Tags     *map[string]bool
	NofTags  int
	Vertical bool
	Used     bool
	Merged   bool
}

func NewPicture(id int, tags *map[string]bool, vertical bool) *Picture {
	return &Picture{
		ID1:      id,
		ID2:      -1,
		Tags:     tags,
		NofTags:  len(*tags),
		Vertical: vertical,
		Used:     false,
		Merged:   false,
	}
}

func (p1 *Picture) Merge(p2 *Picture) {
	p1.ID2 = p2.ID1
	p2.Used = true

	p1.Merged = true
	p2.Merged = true

	p1.Vertical = false
	for k := range *p2.Tags {
		(*p1.Tags)[k] = true
	}
}

type Slideshow struct {
	NofSlides int
	Out       string
}

func NewSlideshow() *Slideshow {
	return &Slideshow{0, ""}
}

func (s *Slideshow) add(p *Picture) {
	s.NofSlides++
	p.Used = true
	id := ""
	if p.ID2 != -1 {
		id = strconv.Itoa(p.ID1) + " " + strconv.Itoa(p.ID2)
	} else {
		id = strconv.Itoa(p.ID1)
	}
	s.Out += id + "\n"
}
