package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

var picturesMap = map[string][]*Picture{}
var allPictures = []*Picture{}
var vertikale = []*Picture{}

func main() {
	ss := NewSlideshow()
	readData()

	var pic *Picture
	tags := &map[string]bool{}
	for true {
		pic = findNextPhoto(tags)
		if pic == nil {
			break
		}
		if pic.Vertical {
			pic.Used = true
			pic2 := findVertical(tags, pic.Tags)
			ss.add2(pic, pic2)
			tags = pic.Tags
			for k := range *pic2.Tags {
				(*tags)[k] = true
			}
			// fmt.Print("a: ")
			// fmt.Println(tags)
			// fmt.Println(pic.Tags)
			// fmt.Println(pic2.Tags)
			continue
		} else {
			ss.add(pic)
			tags = pic.Tags
		}
	}

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

	// Pojdi cez vse vpise in jih posortiraj v map s tagi ter v seznam vseh slik
	for i := 0; i < n; i++ {
		sc.Scan()
		p := strings.Split(sc.Text(), " ") // prebrana vrstica

		tags := p[2:]                // tagi ki jih imamo
		tagsMap := map[string]bool{} // Set tagov - to se posleje v picutre struct, da lahko cekiramo z if _, ok = map[tag]; ok{ //obstaja }

		jeVertikala := p[0] == "V"
		pic := NewPicture(i, &tagsMap, jeVertikala) // Pointer na trenutn picture
		if jeVertikala {
			vertikale = append(vertikale, pic)
		}

		allPictures = append(allPictures, pic) // dodaj sliko v seznam vseh slik
		for _, t := range tags {
			tagsMap[t] = true                            // dodaj tag v map
			picturesMap[t] = append(picturesMap[t], pic) // dodaj sliko na seznam teh tagov
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
	bestP := &Picture{}
	emptyP := bestP
	bestPoints := 0

	for t := range *tags {
		for _, p := range picturesMap[t] {
			if p.Used {
				continue
			} else if _, ok := checked[p]; ok {
				continue
			} else {
				checked[p] = true
			}
			i := intersectionOfTags(tags, p.Tags)
			a := len(*p.Tags)
			points := min(i, min(a-i, b-i))
			if points > 5 {
				return p
			}
			if points > bestPoints {
				bestP = p
				bestPoints = points
			}
		}
	}

	if bestP != emptyP {
		return bestP
	}
	for _, p := range allPictures {
		if !p.Used {
			return p
		}
	}

	return nil
}

func intersectionOfThreeTags(smallestTags, tags2, tags3 *map[string]bool) int {
	i := 0
	for t := range *smallestTags {
		_, ok := (*tags3)[t]
		if _, ok2 := (*tags2)[t]; ok && ok2 {
			i++
		}
	}
	return i
}

func findVertical(oldTags, picTags *map[string]bool) *Picture {
	a := len(*picTags)
	o := len(*oldTags)
	bestP := &Picture{}
	bestI := 0
	emptyP := bestP
	bestPoints := 0
	presekAO := intersectionOfTags(picTags, oldTags)
	i := -1
	for _, p := range vertikale {
		i++
		if p.Used {
			continue
		}
		b := len(*p.Tags)

		var presekVseTri int
		// this is what AI looks like
		if o > a {
			if a > b {
				presekVseTri = intersectionOfThreeTags(p.Tags, picTags, oldTags)
			} else {
				presekVseTri = intersectionOfThreeTags(picTags, p.Tags, oldTags)
			}
		} else {
			if o > b {
				presekVseTri = intersectionOfThreeTags(p.Tags, picTags, oldTags)
			} else {
				presekVseTri = intersectionOfThreeTags(oldTags, p.Tags, picTags)
			}
		}

		presekBO := intersectionOfTags(oldTags, p.Tags)
		samoO := o - presekAO - presekBO + presekVseTri
		presek := presekBO + presekAO - presekVseTri
		presekNovo := a + b - presekAO - presekBO + 2*presekVseTri - intersectionOfTags(picTags, p.Tags)
		if presek == 0 || samoO == 0 || presekNovo == 0 {
			continue
		}
		points := min(presek, min(samoO, presekNovo))
		if points >= 5 {
			return p
		}
		if points > bestPoints {
			bestPoints = points
			bestP = p
			bestI = i
		}
	}

	if bestP != emptyP {
		vertikale = append(vertikale[:bestI], vertikale[bestI+1:]...)
		return bestP
	}

	// fmt.Println("Dodano")
	for _, p := range vertikale {
		if !p.Used {
			return p
		}
	}
	return nil
}

func min(a, b int) int {
	if a > b {
		return a
	}
	return b
}

type Picture struct {
	ID       int
	Tags     *map[string]bool
	Vertical bool
	Used     bool
}

func NewPicture(id int, tags *map[string]bool, vertical bool) *Picture {
	return &Picture{ID: id, Tags: tags, Vertical: vertical, Used: false}
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
	id := strconv.Itoa(p.ID)
	s.Out += id + "\n"
}

func (s *Slideshow) add2(p1, p2 *Picture) {
	s.NofSlides++
	p1.Used = true
	p2.Used = true
	id := strconv.Itoa(p1.ID) + " " + strconv.Itoa(p2.ID)
	s.Out += id + "\n"
}
