import type { Employee, Skill } from './employee'
import type { Project } from './project'
import type { SearchProfile } from './search'

export const mockSkills: Skill[] = [
  { id: 'java', label: 'Java' },
  { id: 'spring-boot', label: 'Spring Boot' },
  { id: 'react', label: 'React' },
  { id: 'nodejs', label: 'Node.js' },
  { id: 'python', label: 'Python' },
  { id: 'django', label: 'Django' },
  { id: 'ruby', label: 'Ruby' },
  { id: 'rails', label: 'Rails' },
  { id: 'php', label: 'PHP' },
  { id: 'laravel', label: 'Laravel' },
  { id: 'javascript', label: 'JavaScript' },
  { id: 'vuejs', label: 'Vue.js' },
  { id: 'html', label: 'HTML' },
  { id: 'css', label: 'CSS' },
  { id: 'cs', label: 'C#' },
  { id: 'dotnet', label: '.NET' },
  { id: 'swift', label: 'Swift' },
  { id: 'ios', label: 'iOS' },
  { id: 'kotlin', label: 'Kotlin' },
  { id: 'android', label: 'Android' }
]

export const mockSearchProfiles: SearchProfile[] = [
  {
    id: '1',
    startDate: '2021-01-01',
    endDate: '2021-01-31',
    name: 'Profil React Junior',
    experience: [1, 2],
    createdAt: '2021-01-01',
    skillsIds: ['react', 'javascript'],
  },
  {
    id: '2',
    startDate: '2024-02-01',
    endDate: '2024-02-31',
    name: 'Profil Java Senior',
    experience: [5, 29],
    createdAt: '2021-01-01',
    skillsIds: [ 'java'],
  },
  {
    id: '3',
    startDate: '',
    endDate: '',
    name: 'Profil Mobile Dev',
    experience: [],
    createdAt: '2021-01-01',
    skillsIds: [ 'ios','swift'],
  },
  {
    id: '4',
    startDate: '2024-02-01',
    endDate: '2024-02-31',
    name: 'Profil Semaine Pro',
    experience: [],
    createdAt: '2021-01-01',
    skillsIds: [],
  }
]

const mockProjects: Project[] = [
  {
    id: '1',
    name: 'Project 1',
    description: 'Description 1',
    managerId: '1',
    startDate: '2020-01-01',
    endDate: '2020-12-31',
    client: 'Amazon',
    color: '#426896',
    createdAt: '2020-01-01',
    employees: [
      {
        id: '1',
        firstname: 'John',
        lastname: 'Doe',
        email: 'john.doe@gmail.com',
        careerStart: '2020-01-01',
        createdAt: '2020-01-01',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '21',
        firstname: 'Steve',
        lastname: 'Quartz',
        email: 'steve.quartz@gmail.com',
        careerStart: '2022-12-20',
        createdAt: '2000-12-20',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '22',
        firstname: 'Tina',
        lastname: 'Topaz',
        email: 'tina.topaz@gmail.com',
        careerStart: '1999-03-10',
        createdAt: '1999-03-10',
        skills: getRandomElements(mockSkills, 1, 5)
      },
    ]
  },
  {
    id: '2',
    name: 'Project 2',
    description: 'Description 2',
    managerId: '2',
    startDate: '2019-05-15',
    endDate: '2019-05-25',
    client: 'Orange',
    color: '#456789',
    createdAt: '2019-05-15',
    employees: [
      {
        id: '1',
        firstname: 'John',
        lastname: 'Doe',
        email: 'john.doe@gmail.com',
        careerStart: '2020-01-01',
        createdAt: '2020-01-01',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '21',
        firstname: 'Steve',
        lastname: 'Quartz',
        email: 'steve.quartz@gmail.com',
        careerStart: '2022-12-20',
        createdAt: '2000-12-20',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '22',
        firstname: 'Tina',
        lastname: 'Topaz',
        email: 'tina.topaz@gmail.com',
        careerStart: '1999-03-10',
        createdAt: '1999-03-10',
        skills: getRandomElements(mockSkills, 1, 5)
      },
    ]
  },
  {
    id: '3',
    name: 'Project 3',
    description: 'Description 3',
    managerId: '3',
    startDate: '2018-03-10',
    endDate: '2018-12-31',
    client: 'EDF',
    color: '#587798',
    createdAt: '2018-03-10',
    employees: [
      {
        id: '1',
        firstname: 'John',
        lastname: 'Doe',
        email: 'john.doe@gmail.com',
        careerStart: '2020-01-01',
        createdAt: '2020-01-01',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '21',
        firstname: 'Steve',
        lastname: 'Quartz',
        email: 'steve.quartz@gmail.com',
        careerStart: '2022-12-20',
        createdAt: '2000-12-20',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '22',
        firstname: 'Tina',
        lastname: 'Topaz',
        email: 'tina.topaz@gmail.com',
        careerStart: '1999-03-10',
        createdAt: '1999-03-10',
        skills: getRandomElements(mockSkills, 1, 5)
      },
    ]
  },
  {
    id: '4',
    name: 'Project 4',
    description: 'Description 4',
    managerId: '4',
    startDate: '2017-07-20',
    endDate: '2017-12-31',
    client: 'Enedis',
    color: '#789345',
    createdAt: '2017-07-20',
    employees: [
      {
        id: '1',
        firstname: 'John',
        lastname: 'Doe',
        email: 'john.doe@gmail.com',
        careerStart: '2020-01-01',
        createdAt: '2020-01-01',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '21',
        firstname: 'Steve',
        lastname: 'Quartz',
        email: 'steve.quartz@gmail.com',
        careerStart: '2022-12-20',
        createdAt: '2000-12-20',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '22',
        firstname: 'Tina',
        lastname: 'Topaz',
        email: 'tina.topaz@gmail.com',
        careerStart: '1999-03-10',
        createdAt: '1999-03-10',
        skills: getRandomElements(mockSkills, 1, 5)
      },
    ]
  },
  {
    id: '5',
    name: 'Project 5',
    description: 'Description 5',
    managerId: '5',
    startDate: '2015-11-30',
    endDate: '2016-12-31',
    client: 'CGI',
    color: '#173542',
    createdAt: '2016-11-30',
    employees: [
      {
        id: '1',
        firstname: 'John',
        lastname: 'Doe',
        email: 'john.doe@gmail.com',
        careerStart: '2020-01-01',
        createdAt: '2020-01-01',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '21',
        firstname: 'Steve',
        lastname: 'Quartz',
        email: 'steve.quartz@gmail.com',
        careerStart: '2022-12-20',
        createdAt: '2000-12-20',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '22',
        firstname: 'Tina',
        lastname: 'Topaz',
        email: 'tina.topaz@gmail.com',
        careerStart: '1999-03-10',
        createdAt: '1999-03-10',
        skills: getRandomElements(mockSkills, 1, 5)
      },
    ]
  },
  {
    id: '6',
    name: 'Project 6',
    description: 'Description 6',
    managerId: '6',
    startDate: '2015-02-25',
    endDate: '2015-12-31',
    client: "L'entreprise de parking d'Aurélien",
    color: '#FF5858',
    createdAt: '2015-02-25',
    employees: [
      {
        id: '1',
        firstname: 'John',
        lastname: 'Doe',
        email: 'john.doe@gmail.com',
        careerStart: '2020-01-01',
        createdAt: '2020-01-01',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '21',
        firstname: 'Steve',
        lastname: 'Quartz',
        email: 'steve.quartz@gmail.com',
        careerStart: '2022-12-20',
        createdAt: '2000-12-20',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '22',
        firstname: 'Tina',
        lastname: 'Topaz',
        email: 'tina.topaz@gmail.com',
        careerStart: '1999-03-10',
        createdAt: '1999-03-10',
        skills: getRandomElements(mockSkills, 1, 5)
      },
    ]
  },
  {
    id: '7',
    name: 'Project 7',
    description: 'Description 7',
    managerId: '1',
    startDate: '2014-08-15',
    endDate: '2014-12-31',
    client: 'La Poste',
    color: '#148615',
    createdAt: '2014-08-15',
    employees: [
      {
        id: '1',
        firstname: 'John',
        lastname: 'Doe',
        email: 'john.doe@gmail.com',
        careerStart: '2020-01-01',
        createdAt: '2020-01-01',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '21',
        firstname: 'Steve',
        lastname: 'Quartz',
        email: 'steve.quartz@gmail.com',
        careerStart: '2022-12-20',
        createdAt: '2000-12-20',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '22',
        firstname: 'Tina',
        lastname: 'Topaz',
        email: 'tina.topaz@gmail.com',
        careerStart: '1999-03-10',
        createdAt: '1999-03-10',
        skills: getRandomElements(mockSkills, 1, 5)
      },
    ]
  },
  {
    id: '8',
    name: 'Project 8',
    description: 'Description 8',
    managerId: '2',
    startDate: '2020-05-10',
    endDate: '2020-12-31',
    client: 'La Banque Postale',
    color: '#FF5858',
    createdAt: '2013-05-10',
    employees: [
      {
        id: '1',
        firstname: 'John',
        lastname: 'Doe',
        email: 'john.doe@gmail.com',
        careerStart: '2020-01-01',
        createdAt: '2020-01-01',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '21',
        firstname: 'Steve',
        lastname: 'Quartz',
        email: 'steve.quartz@gmail.com',
        careerStart: '2022-12-20',
        createdAt: '2000-12-20',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '22',
        firstname: 'Tina',
        lastname: 'Topaz',
        email: 'tina.topaz@gmail.com',
        careerStart: '1999-03-10',
        createdAt: '1999-03-10',
        skills: getRandomElements(mockSkills, 1, 5)
      },
    ]
  },
  {
    id: '9',
    name: 'Project 9',
    description: 'Description 9',
    managerId: '3',
    startDate: '2012-09-05',
    endDate: '2012-12-31',
    client: 'Capgemini',
    color: '#FF5878',
    createdAt: '2012-09-05',
    employees: [
      {
        id: '1',
        firstname: 'John',
        lastname: 'Doe',
        email: 'john.doe@gmail.com',
        careerStart: '2020-01-01',
        createdAt: '2020-01-01',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '21',
        firstname: 'Steve',
        lastname: 'Quartz',
        email: 'steve.quartz@gmail.com',
        careerStart: '2022-12-20',
        createdAt: '2000-12-20',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '22',
        firstname: 'Tina',
        lastname: 'Topaz',
        email: 'tina.topaz@gmail.com',
        careerStart: '1999-03-10',
        createdAt: '1999-03-10',
        skills: getRandomElements(mockSkills, 1, 5)
      },
    ]
  },
  {
    id: '10',
    name: 'Project 10',
    description: 'Description 10',
    managerId: '4',
    startDate: '2014-12-20',
    endDate: '2014-12-31',
    client: 'Agelia',
    color: '#152548',
    createdAt: '2011-12-20',
    employees: [
      {
        id: '1',
        firstname: 'John',
        lastname: 'Doe',
        email: 'john.doe@gmail.com',
        careerStart: '2020-01-01',
        createdAt: '2020-01-01',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '21',
        firstname: 'Steve',
        lastname: 'Quartz',
        email: 'steve.quartz@gmail.com',
        careerStart: '2022-12-20',
        createdAt: '2000-12-20',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '22',
        firstname: 'Tina',
        lastname: 'Topaz',
        email: 'tina.topaz@gmail.com',
        careerStart: '1999-03-10',
        createdAt: '1999-03-10',
        skills: getRandomElements(mockSkills, 1, 5)
      },
    ]
  },
  {
    id: '11',
    name: 'Project 11',
    description: 'Description 11',
    managerId: '5',
    startDate: '2010-04-10',
    endDate: '2010-12-31',
    client: 'SFR',
    color: '#975342',
    createdAt: '2010-04-10',
    employees: [
      {
        id: '1',
        firstname: 'John',
        lastname: 'Doe',
        email: 'john.doe@gmail.com',
        careerStart: '2020-01-01',
        createdAt: '2020-01-01',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '21',
        firstname: 'Steve',
        lastname: 'Quartz',
        email: 'steve.quartz@gmail.com',
        careerStart: '2022-12-20',
        createdAt: '2000-12-20',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '22',
        firstname: 'Tina',
        lastname: 'Topaz',
        email: 'tina.topaz@gmail.com',
        careerStart: '1999-03-10',
        createdAt: '1999-03-10',
        skills: getRandomElements(mockSkills, 1, 5)
      },
    ]
  },
  {
    id: '12',
    name: 'Project 12',
    description: 'Description 12',
    managerId: '6',
    startDate: '2021-07-25',
    endDate: '2021-12-31',
    client: 'Nokia',
    color: '#487214',
    createdAt: '2009-07-25',
    employees: [
      {
        id: '1',
        firstname: 'John',
        lastname: 'Doe',
        email: 'john.doe@gmail.com',
        careerStart: '2020-01-01',
        createdAt: '2020-01-01',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '21',
        firstname: 'Steve',
        lastname: 'Quartz',
        email: 'steve.quartz@gmail.com',
        careerStart: '2022-12-20',
        createdAt: '2000-12-20',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '22',
        firstname: 'Tina',
        lastname: 'Topaz',
        email: 'tina.topaz@gmail.com',
        careerStart: '1999-03-10',
        createdAt: '1999-03-10',
        skills: getRandomElements(mockSkills, 1, 5)
      },
    ]
  },
  {
    id: '13',
    name: 'Project 13',
    description: 'Description 13',
    managerId: '1',
    startDate: '2008-11-15',
    endDate: '2008-12-31',
    client: 'Orange',
    color: '#FF5858',
    createdAt: '2008-11-15',
    employees: [
      {
        id: '1',
        firstname: 'John',
        lastname: 'Doe',
        email: 'john.doe@gmail.com',
        careerStart: '2020-01-01',
        createdAt: '2020-01-01',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '21',
        firstname: 'Steve',
        lastname: 'Quartz',
        email: 'steve.quartz@gmail.com',
        careerStart: '2022-12-20',
        createdAt: '2000-12-20',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '22',
        firstname: 'Tina',
        lastname: 'Topaz',
        email: 'tina.topaz@gmail.com',
        careerStart: '1999-03-10',
        createdAt: '1999-03-10',
        skills: getRandomElements(mockSkills, 1, 5)
      },
    ]
  },
  {
    id: '14',
    name: 'Project 14',
    description: 'Description 14',
    managerId: '2',
    startDate: '2019-03-05',
    endDate: '2019-12-31',
    client: 'EDF',
    color: '#FF5858',
    createdAt: '2007-03-05',
    employees: [
      {
        id: '1',
        firstname: 'John',
        lastname: 'Doe',
        email: 'john.doe@gmail.com',
        careerStart: '2020-01-01',
        createdAt: '2020-01-01',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '21',
        firstname: 'Steve',
        lastname: 'Quartz',
        email: 'steve.quartz@gmail.com',
        careerStart: '2022-12-20',
        createdAt: '2000-12-20',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '22',
        firstname: 'Tina',
        lastname: 'Topaz',
        email: 'tina.topaz@gmail.com',
        careerStart: '1999-03-10',
        createdAt: '1999-03-10',
        skills: getRandomElements(mockSkills, 1, 5)
      },
    ]
  },
  {
    id: '15',
    name: 'Project 15',
    description: 'Description 15',
    managerId: '3',
    startDate: '2006-06-20',
    endDate: '2006-12-31',
    client: 'Amazon',
    color: '#FF5858',
    createdAt: '2006-06-20',
    employees: [
      {
        id: '1',
        firstname: 'John',
        lastname: 'Doe',
        email: 'john.doe@gmail.com',
        careerStart: '2020-01-01',
        createdAt: '2020-01-01',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '21',
        firstname: 'Steve',
        lastname: 'Quartz',
        email: 'steve.quartz@gmail.com',
        careerStart: '2022-12-20',
        createdAt: '2000-12-20',
        skills: getRandomElements(mockSkills, 1, 5)
      },
      {
        id: '22',
        firstname: 'Tina',
        lastname: 'Topaz',
        email: 'tina.topaz@gmail.com',
        careerStart: '1999-03-10',
        createdAt: '1999-03-10',
        skills: getRandomElements(mockSkills, 1, 5)
      },
    ]
  }
]

function getRandomElements<T>(arr: T[], min: number, max: number): T[] {
  const count = Math.floor(Math.random() * (max - min + 1)) + min
  const shuffled = arr.sort(() => 0.5 - Math.random())
  return shuffled.slice(0, count)
}

const mockEmployees: Employee[] = [
  {
    id: '1',
    firstname: 'John',
    lastname: 'Doe',
    email: 'john.doe@gmail.com',
    careerStart: '2020-01-01',
    createdAt: '2020-01-01',
    urlCV: 'https://www.google.com',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '2',
    firstname: 'Jane',
    lastname: 'Smith',
    email: 'jane.smith@gmail.com',
    careerStart: '2019-05-15',
    createdAt: '2019-05-15',
    urlCV: 'https://www.google.com',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '3',
    firstname: 'Alice',
    lastname: 'Brown',
    email: 'alice.brown@gmail.com',
    careerStart: '2018-03-10',
    createdAt: '2018-03-10',
    urlCV: 'https://www.cv.com',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '4',
    firstname: 'Bob',
    lastname: 'White',
    email: 'bob.white@gmail.com',
    careerStart: '2017-07-20',
    createdAt: '2017-07-20',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '5',
    firstname: 'Charlie',
    lastname: 'Black',
    email: 'charlie.black@gmail.com',
    careerStart: '2015-11-30',
    createdAt: '2016-11-30',
    urlCV: 'https://www.cv.com',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '6',
    firstname: 'David',
    lastname: 'Green',
    email: 'david.green@gmail.com',
    careerStart: '2015-02-25',
    createdAt: '2015-02-25',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '7',
    firstname: 'Eve',
    lastname: 'Grey',
    email: 'eve.grey@gmail.com',
    careerStart: '2014-08-15',
    createdAt: '2014-08-15',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '8',
    firstname: 'Frank',
    lastname: 'Orange',
    email: 'frank.orange@gmail.com',
    careerStart: '2020-05-10',
    createdAt: '2013-05-10',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '9',
    firstname: 'Grace',
    lastname: 'Pink',
    email: 'grace.pink@gmail.com',
    careerStart: '2012-09-05',
    createdAt: '2012-09-05',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '10',
    firstname: 'Henry',
    lastname: 'Red',
    email: 'henry.red@gmail.com',
    careerStart: '2014-12-20',
    createdAt: '2011-12-20',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '11',
    firstname: 'Ivy',
    lastname: 'Yellow',
    email: 'ivy.yellow@gmail.com',
    careerStart: '2010-04-10',
    createdAt: '2010-04-10',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '12',
    firstname: 'Jack',
    lastname: 'Blue',
    email: 'jack.blue@gmail.com',
    careerStart: '2021-07-25',
    createdAt: '2009-07-25',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '13',
    firstname: 'Kelly',
    lastname: 'Cyan',
    email: 'kelly.cyan@gmail.com',
    careerStart: '2008-11-15',
    createdAt: '2008-11-15',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '14',
    firstname: 'Larry',
    lastname: 'Magenta',
    email: 'larry.magenta@gmail.com',
    careerStart: '2019-03-05',
    createdAt: '2007-03-05',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '15',
    firstname: 'Mary',
    lastname: 'Gold',
    email: 'mary.gold@gmail.com',
    careerStart: '2006-06-20',
    createdAt: '2006-06-20',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '16',
    firstname: 'Nancy',
    lastname: 'Silver',
    email: 'nancy.silver@gmail.com',
    careerStart: '2015-09-10',
    createdAt: '2005-09-10',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '17',
    firstname: 'Olive',
    lastname: 'Bronze',
    email: 'olive.bronze@gmail.com',
    careerStart: '2004-12-05',
    createdAt: '2004-12-05',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '18',
    firstname: 'Peter',
    lastname: 'Platinum',
    email: 'peter.platinum@gmail.com',
    careerStart: '2012-03-15',
    createdAt: '2003-03-15',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '19',
    firstname: 'Queen',
    lastname: 'Sapphire',
    email: 'queen.sapphire@gmail.com',
    careerStart: '2002-06-10',
    createdAt: '2002-06-10',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '20',
    firstname: 'Roger',
    lastname: 'Emerald',
    email: 'roger.emerald@gmail.com',
    careerStart: '2022-09-05',
    createdAt: '2001-09-05',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '21',
    firstname: 'Steve',
    lastname: 'Quartz',
    email: 'steve.quartz@gmail.com',
    careerStart: '2022-12-20',
    createdAt: '2000-12-20',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '22',
    firstname: 'Tina',
    lastname: 'Topaz',
    email: 'tina.topaz@gmail.com',
    careerStart: '1999-03-10',
    createdAt: '1999-03-10',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '23',
    firstname: 'Ursula',
    lastname: 'Amethyst',
    email: 'ursula.amethyst@gmail.com',
    careerStart: '2015-06-25',
    createdAt: '1998-06-25',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '24',
    firstname: 'Victor',
    lastname: 'Citrine',
    email: 'victor.citrine@gmail.com',
    careerStart: '1997-09-15',
    createdAt: '1997-09-15',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  },
  {
    id: '25',
    firstname: 'Wendy',
    lastname: 'Coral',
    email: 'wendy.coral@gmail.com',
    careerStart: '1996-12-05',
    createdAt: '1996-12-05',
    skills: getRandomElements(mockSkills, 1, 5),
    projects: getRandomElements(mockProjects, 0, 5)
  }
]

export { mockEmployees, mockProjects }

