import os
import random
import requests

book_ids = [
    1342, 
    11,   
    1661, 
    84,  
    2701, 
    345,  
    98,   
    43,   
    76,   
    74,  
    1232,  
    5200, 
    174,  
    1260, 
    2591, 
    1080,
    1400,
    219, 
    205, 
    160,  
    1399,   
    1513,
    100,
    25344,
    145,
    37106,
    6593,
    2600,
    1727,
    120,
    779,
    135,
    10007,
    8438,
    26,
    829,
    996,
    730,
    27673,
    10,
    1524,
    521,
    36,
    164,
    21700,
    1934,
    766,
    1001,
    1257,
    160,
    18857,
    5921,
    599,
    17135,
    121,
    1837,
    203,
    2527,
    20,
    103,
    24022,
    1636,
    51356,
    1322,
    23700,
    1533,
    215,
    22120,
    34206
    
    
    
    
    
]

inserts=["anticonservationists","antiauthoritarianism","compartmentalization","comprehensiblenesses","counterdemonstration","counterintelligences","crystallographically","departmentalizations","distinguishabilities","expressionlessnesses","existentialistically","forethoughtfulnesses","hypersensitivenesses","inappreciativenesses","indestructiblenesses","indiscriminatenesses","interchangeabilities","internationalization","interconnectednesses","nondestructivenesses","noninstitutionalized","overdifferentiations","overintellectualized","overprotectivenesses","professionalizations","psychopathologically","representationalisms","reinstitutionalizing","transformationalists","unintelligiblenesses","unsatisfactorinesses","unconscionablenesses","unrepresentativeness"]
def get_book():
    
    book_id = random.choice(book_ids)
    url = f"https://www.gutenberg.org/files/{book_id}/{book_id}-0.txt"
    
    try:
        response = requests.get(url)
        response.raise_for_status()
        text = response.text
        
        startt = "*** START OF THE PROJECT GUTENBERG EBOOK"
        endd = "*** END OF THE PROJECT GUTENBERG EBOOK"
        
        starti = text.find(startt)
        if starti != -1:
            text = text[starti + len(startt):]
        
        endi = text.find(endd)
        if endi != -1:
            text = text[:endi]
        
        text=text.replace("-"," ")
        text=text.replace("—"," ")
        text=text.replace("/"," ")
        text=text.replace("Wahlverwan","Wahlverwan ")
        text=text.replace("Goodnessgrac","Goodnessgr ac")
        text=text.replace("honorificabili","honorificabili ")
        

        

        
        return text.strip() 
    except requests.RequestException:
        return ""


def get_random_untill():
    tot=""
    wordc=0
    target=2000000
    bookc=0
    while wordc<target:
        book_id=random.choice(book_ids)
        book_txt=get_book(book_id)
        
        if book_txt:
            tot+=book_txt+"\n\n"
            bookc+=1
            
            if bookc%2==0:
                rword=random.choice(inserts)
                tot+=rword+"\n\n"
            wordc=len(tot.split())
        else:
            continue
    return tot.strip()
            

def create_random_structure(base_path, depth=1, max_depth=15):
    if depth > max_depth:
        return
    
    num_items = random.randint(0, 5)

    for _ in range(num_items):
        choice = random.choice(["folder", "file"])

        if choice == "folder":
            folder_name = f"folder_{random.randint(1000, 9999)}"
            folder_path = os.path.join(base_path, folder_name)
            os.makedirs(folder_path, exist_ok=True)

            create_random_structure(folder_path, depth + 1, max_depth)

        else: 
            file_name = f"file_{random.randint(1000, 9999)}.txt"
            file_path = os.path.join(base_path, file_name)

            with open(file_path, "w", encoding="utf-8") as f:
                book_content = get_book()
                f.write(book_content + "\n")


root_dir = "generated_structure"
os.makedirs(root_dir, exist_ok=True)

create_random_structure(root_dir)

print("success!")
